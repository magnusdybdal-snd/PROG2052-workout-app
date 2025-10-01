package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.repositories.ExercisesRepository
import javax.inject.Inject

class GetExercisesUseCase @Inject constructor(
    private val repository: ExercisesRepository
) {
    suspend operator fun invoke(): List<Exercise> {
        return repository.getExercises()
    }
}
