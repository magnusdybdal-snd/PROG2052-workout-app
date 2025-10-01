package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import javax.inject.Inject

class GetHistoryWorkoutUseCase @Inject constructor(
    private val repository: HistoryWorkoutRepository
) {
    suspend operator fun invoke(): List<HistoryWorkout> {
        return repository.getHistoryWorkouts()
    }
}