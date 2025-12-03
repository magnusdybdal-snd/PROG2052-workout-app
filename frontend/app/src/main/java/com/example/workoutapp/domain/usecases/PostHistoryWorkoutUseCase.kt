package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import javax.inject.Inject

/**
 * Use case for saving a completed workout session to history.
 *
 * Called when the user finishes a workout to persist the performance data
 * (exercises, sets, reps, weight, duration, notes) for future reference and tracking.
 *
 * @property repository The history workout repository for data access
 */
class PostHistoryWorkoutUseCase @Inject constructor(
    private val repository: HistoryWorkoutRepository
) {
    /**
     * Saves a completed workout session to history.
     *
     * The session is saved to local Room database first, then synced
     * to the backend API for backup and cross-device synchronization.
     *
     * @param session The completed workout session to save
     */
    suspend operator fun invoke(session: Session) {
        repository.postHistoryWorkout(session)
    }
}
