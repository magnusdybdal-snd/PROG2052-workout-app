package com.example.workoutapp.data.repositories

import android.util.Log
import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.database.dao.HistoryWorkoutDao
import com.example.workoutapp.data.database.entities.HistoryWorkoutEntity
import com.example.workoutapp.data.database.entities.SetEntity
import com.example.workoutapp.data.database.entities.WorkoutExerciseEntity
import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.models.Set
import com.example.workoutapp.domain.models.WorkoutExercise
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
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
                            exercise = Exercise(
                                exerciseId = exerciseWithSets.exercise.exerciseId,
                                name = exerciseWithSets.exercise.name,
                                targetMuscles = exerciseWithSets.exercise.targetMuscles,
                                bodyParts = exerciseWithSets.exercise.bodyParts,
                                equipments = exerciseWithSets.exercise.equipments,
                                secondaryMuscles = exerciseWithSets.exercise.secondaryMuscles,
                                gifUrl = exerciseWithSets.exercise.gifUrl,
                                instructions = exerciseWithSets.exercise.instructions
                            ),
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
            val session = Session(
                sessionId = local.id,
                name = local.name,
                exercises = emptyList(), //TODO: add nested data later
                duration = local.duration,
                date = local.date,
                note = local.note ?: ""
            )
            try {
                api.postHistoryWorkout(session)
                dao.markAsSynced(local.id)
            } catch (_: Exception) {
                Log.w("Repo", "Failed to sync workout ${local.id}")
            }
        }

        // Step 2: Pull latest from API
        val remoteWorkouts = try {
            api.getHistoryWorkouts().map { dto ->
                val localTime = LocalTime.parse(dto.duration)
                val duration = Duration.ofSeconds(localTime.toSecondOfDay().toLong())

                val workoutEntity = HistoryWorkoutEntity(
                    id = dto.historyWorkoutId,
                    name = dto.name,
                    date = LocalDate.parse(dto.date),
                    duration = duration,
                    note = dto.note,
                    isSynced = true
                )

                // Nested entities
                val exerciseEntities = dto.exercises.map { exDto ->
                    WorkoutExerciseEntity(
                        workoutId = dto.historyWorkoutId,
                        exerciseId = exDto.exercise.exerciseId,
                        name = exDto.exercise.name,
                        targetMuscles = exDto.exercise.targetMuscles,
                        bodyParts = exDto.exercise.bodyParts,
                        equipments = exDto.exercise.equipments,
                        secondaryMuscles = exDto.exercise.secondaryMuscles,
                        gifUrl = exDto.exercise.gifUrl,
                        instructions = exDto.exercise.instructions
                    )
                }

                val setEntities = dto.exercises.flatMapIndexed { idx, exDto ->
                    exDto.sets.map { setDto ->
                        SetEntity(
                            exerciseEntityId = idx + 1, // temporary — fixed by foreign key later
                            rep = setDto.rep,
                            kg = setDto.kg,
                            typeSet = setDto.typeSet
                        )
                    }
                }
                Triple(workoutEntity, exerciseEntities, setEntities)
            }
        } catch (e: Exception) {
            Log.e("Repo", "Error fetching workouts from API: ${e.message}")
            emptyList()
        }

        // Step 3: Insert locally (nested)
        remoteWorkouts.forEach { (workout, exercises, sets) ->
            try {
                dao.insertFullWorkout(workout, exercises, sets)
            } catch (e: Exception) {
                Log.e("Repo", "Failed to insert full workout ${workout.id}: ${e.message}")
            }
        }

        Log.d("Repo", "Fetched ${remoteWorkouts.size} remote workouts")
        Log.d("Repo", "Local DB now has ${dao.getAllHistoryWorkoutsSnapshot().size} workouts")

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
        val entity = HistoryWorkoutEntity(
            id = session.sessionId,
            name = session.name,
            date = session.date,
            duration = session.duration,
            note = session.note,
            isSynced = true
        )
        dao.insert(entity)

        // Try to push new session
        try {
            api.postHistoryWorkout(session)
            dao.markAsSynced(session.sessionId)
        } catch (_: Exception) {
            Log.w("Repo", "Workout queued for sync: ${session.sessionId}")
        }

    }
}