package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import jakarta.inject.Inject

class DeleteHistoryWorkoutUseCase @Inject constructor(
    private val repository: HistoryWorkoutRepository
) {
    suspend operator fun invoke(historyWorkout: HistoryWorkout) {
        repository.deleteHistoryWorkout(historyWorkout)
    }
}
