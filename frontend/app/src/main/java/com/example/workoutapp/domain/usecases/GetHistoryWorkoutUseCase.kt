package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
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
     * Returns a Flow of all workouts from local storage.
     * Also triggers a remote fetch once on start to ensure latest data.
     */
    suspend operator fun invoke(): Flow<List<HistoryWorkout>> {
        return repository.observeHistoryWorkouts()
            .onStart {
                try {
                    repository.getHistoryWorkouts()
            } catch (_: Exception) {
                // Fail silently - offline mode still works
            }
        }
    }
}