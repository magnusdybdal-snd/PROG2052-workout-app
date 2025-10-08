package com.example.workoutapp.domain.usecases

import com.example.workoutapp.data.api.dto.HistoryWorkoutDto
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import javax.inject.Inject

class PostHistoryWorkoutUseCase @Inject constructor(
    private val repository: HistoryWorkoutRepository
) {
    suspend operator fun invoke(historyWorkoutDto: HistoryWorkoutDto) {
        repository.postHistoryWorkout(historyWorkoutDto)
    }
}
