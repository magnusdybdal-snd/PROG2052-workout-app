package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.repositories.ExercisesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExercisesUseCase @Inject constructor(
    private val repository: ExercisesRepository
) {
    suspend operator fun invoke(): Flow<List<Exercise>> {
        return repository.observeExercises()
    }
}
