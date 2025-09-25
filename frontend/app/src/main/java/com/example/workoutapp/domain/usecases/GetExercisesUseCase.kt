package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.repositories.ExerciseRepository

class GetExercisesUseCase(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(): List<Exercise> {
        return repository.getExercises()
    }
}
