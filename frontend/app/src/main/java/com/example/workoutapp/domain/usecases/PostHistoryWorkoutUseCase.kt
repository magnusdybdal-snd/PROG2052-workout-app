package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.repositories.HistoryWorkoutRepository
import javax.inject.Inject

class PostHistoryWorkoutUseCase @Inject constructor(
    private val repository: HistoryWorkoutRepository
) {
    suspend operator fun invoke(session: Session) {
        repository.postHistoryWorkout(session)
    }
}
