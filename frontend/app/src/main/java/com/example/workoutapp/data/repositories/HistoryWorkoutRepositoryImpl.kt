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
     */
    override suspend fun getHistoryWorkouts(): List<HistoryWorkout> {
        val remoteWorkouts = api.getHistoryWorkouts().map { dto ->
            val localTime = LocalTime.parse(dto.duration)
            val duration = Duration.ofSeconds(localTime.toSecondOfDay().toLong())

            HistoryWorkoutEntity(
                id = dto.historyWorkoutId,
                name = dto.name,
                date = LocalDate.parse(dto.date),
                duration = duration,
                note = dto.note,
                totalVolume = 0.0, // TODO: calculate on demand or store?
                isSynced = true
            )
        }

        // Replace local data with fresh API data
        dao.clearAll()
        dao.insertAll(remoteWorkouts)

        // Return the domain model for use in viewmodel
        return remoteWorkouts.map { entity ->
            HistoryWorkout(
                id = entity.id,
                name = entity.name,
                date = entity.date,
                duration = entity.duration,
                note = entity.note,
                exercises = emptyList() // TODO: Add nested structure
            )
        }
    }


    override suspend fun postHistoryWorkout(session: Session) {
        api.postHistoryWorkout(session)

        val entity = HistoryWorkoutEntity(
            name = session.name,
            date = session.date,            // TODO: use LocalDate for date not String      @see Session.kt
            duration = session.duration,    // TODO: use Duration for duration not String   @see Session.kt
            totalVolume = 0.0, // Todo: calculate?,
            note = session.note,
            isSynced = true
        )
        dao.insert(entity)
    }
}