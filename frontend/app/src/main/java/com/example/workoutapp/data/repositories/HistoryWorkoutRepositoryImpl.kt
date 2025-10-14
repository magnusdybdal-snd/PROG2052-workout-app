package com.example.workoutapp.data.repositories

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.database.dao.HistoryWorkoutDao
import com.example.workoutapp.data.database.entities.HistoryWorkoutEntity
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
    fun observeHistoryWorkouts(): Flow<List<HistoryWorkout>> {

        return dao.getAllHistoryWorkouts().map { entities ->
            entities.map { entity ->
                HistoryWorkout(
                    id = entity.id,
                    name = entity.name,
                    date = entity.date,
                    duration = entity.duration,
                    note = entity.note,
                    exercises = emptyList() // TODO: Add nested exercises and sets
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
        unsyncedWorkouts.forEach { entity ->
            val session = Session(
                sessionId = entity.id,
                name = entity.name,
                exercises = emptyList(), //TODO: add nested data later
                duration = entity.duration,
                date = entity.date,
                note = entity.note
            )
            try {
                api.postHistoryWorkout(session)
                dao.markAsSynced(entity.id)
            } catch (_: Exception) {
                // Stay unsynced if offline or error
            }
        }

        // Step 2: Pull latest from API
        val remoteWorkouts = try {
            api.getHistoryWorkouts().map { dto ->
                HistoryWorkoutEntity(
                    id = dto.historyWorkoutId,
                    name = dto.name,
                    date = LocalDate.parse(dto.date),
                    duration = Duration.parse(dto.duration),
                    note = dto.note,
                    isSynced = true
                )
            }
        } catch (_: Exception) {
            emptyList()
        }

        // Step 3: Merge local and remote
        if (remoteWorkouts.isNotEmpty()){
            dao.clearAll()
            dao.insertAll(remoteWorkouts)

        }

        // Step 4: Return local data from DB (Local first)
        return dao.getAllHistoryWorkoutsSnapshot().map { entity ->
            HistoryWorkout(
                id = entity.id,
                name = entity.name,
                date = entity.date,
                duration = entity.duration,
                note = entity.note,
                exercises = emptyList() // TODO: add nested stuff
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
            // Remains unsynced until next sync attempt
        }

    }
}