package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import jakarta.inject.Inject

/**
 * Use case for deleting a workout from history.
 *
 * Allows users to remove past workout sessions from their history,
 * deleting from both local and remote storage.
 *
 * @property repository The history workout repository for data access
 */
class DeleteHistoryWorkoutUseCase @Inject constructor(
    private val repository: HistoryWorkoutRepository
) {
    /**
     * Deletes a workout from history.
     *
     * The workout is removed from local Room database and deleted
     * from the backend API.
     *
     * @param historyWorkout The history workout to delete
     */
    suspend operator fun invoke(historyWorkout: HistoryWorkout) {
        repository.deleteHistoryWorkout(historyWorkout)
    }
}
