package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving workout history (local - first)
 *
 * Observes local database changes as a flow (auto updates UI)
 * Optionally triggers a background sync with the backend API
 */
class GetHistoryWorkoutUseCase @Inject constructor(
    private val repository: HistoryWorkoutRepository
) {

    /**
     * Observe the local database for workout changes (reactive)
     */
    operator fun invoke(): Flow<List<HistoryWorkout>> {
        return repository.observeHistoryWorkouts()
    }

    /**
     * Perform a manual sync form remote API to local database
     */
    suspend fun syncNow(): List<HistoryWorkout> {
        return repository.getHistoryWorkouts()
    }
}