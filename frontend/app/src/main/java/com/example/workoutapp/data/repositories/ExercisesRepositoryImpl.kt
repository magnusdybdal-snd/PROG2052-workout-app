package com.example.workoutapp.data.repositories

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.domain.repositories.ExerciseRepository
import com.example.workoutapp.domain.models.Exercise

class ExerciseRepositoryImpl(
    private val apiService: ApiService
) : ExerciseRepository {

    override suspend fun getExercises(): List<Exercise> {
        return apiService.getExercises().map { it.toDomain() }
    }
}

