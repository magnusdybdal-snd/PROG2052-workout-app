package com.example.workoutapp.data.repositories

import android.util.Log
import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.database.dao.history.HistoryWorkoutDao
import com.example.workoutapp.data.database.entities.history.HistoryWorkoutEntity
import com.example.workoutapp.data.database.entities.history.SetEntity
import com.example.workoutapp.data.database.entities.history.WorkoutExerciseEntity
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.models.SessionExercise
import com.example.workoutapp.domain.models.Set
import com.example.workoutapp.domain.models.WorkoutExercise
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

/**
 * Repository that provides access to workout history from both local Room DB and remote API.
 * Room acts as the single source of truth, while the API syncs data in and out.
 */
class HistoryWorkoutRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: HistoryWorkoutDao
): HistoryWorkoutRepository {

    /**
     * Observers all local workouts (used by viewmodel) The Flow will automatically emit
     * new changes when there are changes to the DB so view models that observes trough this
     * function will be updated automatically and therefore update UI automatically
     */
    override fun observeHistoryWorkouts(): Flow<List<HistoryWorkout>> {

        return dao.getAllHistoryWorkoutsWithExercises().map { workoutWithExercises ->
            workoutWithExercises.map { fullWorkout ->
                HistoryWorkout(
                    id = fullWorkout.workout.id,
                    name = fullWorkout.workout.name,
                    date = fullWorkout.workout.date,
                    duration = fullWorkout.workout.duration,
                    note = fullWorkout.workout.note,
                    exercises = fullWorkout.exercises.map { exerciseWithSets ->
                        WorkoutExercise(
                            exerciseId = exerciseWithSets.exercise.exerciseId,
                            name = exerciseWithSets.exercise.name,
                            sets = exerciseWithSets.sets.map { setEntity ->
                                Set(
                                    rep = setEntity.rep,
                                    kg = setEntity.kg,
                                    typeSet = setEntity.typeSet
                                )
                            }
                        )
                    }
                )
            }
        }
    }

    /**
     * Fetch HistoryWorkouts from the API and update the local database with fresh API data
     * 1. Push unsynced local workouts to the API
     * 2. Pull updated workouts from the API and merge them locally
     */
    override suspend fun getHistoryWorkouts(): List<HistoryWorkout> {
        // Step 1: Post unsynced workouts to API
        val unsyncedWorkouts = dao.getUnsyncedWorkouts()
        unsyncedWorkouts.forEach { local ->

            // Fetch the full workout with exercises from room
            val fullWorkout = dao.getWorkoutHistoryWithExercises(local.id)

            if (fullWorkout != null) {
                val session = Session(
                    sessionId = local.id,
                    name = local.name,
                    exercises = fullWorkout.exercises.map { exerciseWithSets ->
                        SessionExercise(
                            exerciseId = exerciseWithSets.exercise.exerciseId,
                            name = exerciseWithSets.exercise.name,
                            sets = exerciseWithSets.sets.map { setEntity ->
                                Set(
                                    rep = setEntity.rep,
                                    kg = setEntity.kg,
                                    typeSet = setEntity.typeSet
                                )
                            }
                        )
                    },
                    duration = local.duration,
                    date = local.date,
                    note = local.note
                )

                try {
                    api.postHistoryWorkout(session)
                    dao.markAsSynced(local.id)
                } catch (_: Exception) {
                    Log.w("HistoryRepo", "Failed to sync workout ${local.id}")
                }
            }
        }

        // Step 2: Pull latest from API
        val remoteWorkouts = try {
            api.getHistoryWorkouts().map { dto ->
                val duration = try {
                    Duration.parse(dto.duration)
                } catch (e: Exception) {
                    Log.w("HistoryRepo", "parsing og duration failed: ${e.message}, got duration: ${dto.duration}")
                    val localTime = LocalTime.parse(dto.duration)
                    Duration.ofSeconds(localTime.toSecondOfDay().toLong())
                }

                val workoutEntity = HistoryWorkoutEntity(
                    id = dto.historyWorkoutId,
                    name = dto.name,
                    date = LocalDate.parse(dto.date),
                    duration = duration,
                    note = dto.note ?: "",
                    isSynced = true
                )

                // Nested entities
                val exerciseEntities = dto.exercises.map { exDto ->
                    WorkoutExerciseEntity(
                        id = UUID.randomUUID().toString(),
                        workoutId = dto.historyWorkoutId,
                        exerciseId = exDto.exercise.exerciseId,
                        name = exDto.exercise.name
                    )
                }

                val setEntities = dto.exercises.flatMapIndexed { idx, exDto ->
                    val parentExerciseId = exerciseEntities[idx].id

                    exDto.sets.map { setDto ->
                        SetEntity(
                            id = UUID.randomUUID().toString(),
                            exerciseEntityId = parentExerciseId,
                            rep = setDto.rep,
                            kg = setDto.kg,
                            typeSet = setDto.typeSet
                        )
                    }
                }
                Triple(workoutEntity, exerciseEntities, setEntities)
            }
        } catch (e: Exception) {
            Log.e("HistoryRepo", "Error fetching workouts from API: ${e.message}")
            emptyList()
        }

        // Step 3: Insert locally (nested)
        try {
            dao.insertFullWorkout(remoteWorkouts)
        } catch (e: Exception) {
            Log.e("HistoryRepo", "Failed to insert full workout: ${e.message}")
        }

        Log.d("HistoryRepo", "Fetched ${remoteWorkouts.size} remote workouts")
        Log.d("HistoryRepo", "Local DB now has ${dao.getAllHistoryWorkoutsSnapshot().size} workouts")

        // Step 4: Return local data from DB (Local first)
        return dao.getAllHistoryWorkoutsSnapshot().map { entity ->
            HistoryWorkout(
                id = entity.id,
                name = entity.name,
                date = entity.date,
                duration = entity.duration,
                note = entity.note,
                exercises = emptyList() // exercises are handled by Flow observers
            )
        }
    }

    override suspend fun postHistoryWorkout(session: Session) {
        val workoutEntity = HistoryWorkoutEntity(
            id = session.sessionId,
            name = session.name,
            date = session.date,
            duration = session.duration,
            note = session.note,
            isSynced = false
        )

        // Create exercise entities
        val exerciseEntities = session.exercises.map { sessionExercise ->
            WorkoutExerciseEntity(
                id = UUID.randomUUID().toString(),
                workoutId = session.sessionId,
                exerciseId = sessionExercise.exerciseId,
                name = sessionExercise.name
            )
        }

        // Create set entities
        val setEntities = session.exercises.flatMapIndexed { idx, sessionExercise ->
            val parentExerciseId = exerciseEntities[idx].id

            sessionExercise.sets.map { set ->
                SetEntity(
                    id = UUID.randomUUID().toString(),
                    exerciseEntityId = parentExerciseId,
                    rep = set.rep,
                    kg = set.kg,
                    typeSet = set.typeSet
                )
            }
        }

        // Insert all nested data at once
        dao.insert(workoutEntity)

        // Try to push new session to API
        try {
            api.postHistoryWorkout(session)
            dao.markAsSynced(session.sessionId)
            Log.d("HistoryRepo", "Workout ${session.sessionId} synced successfully")
        } catch (e: Exception) {
            // Workout remains marked as unsynced, will sync later
            Log.e("HistoryRepo", "Workout queued for sync: ${session.sessionId}, error: ${e.message}")
        }
    }
}