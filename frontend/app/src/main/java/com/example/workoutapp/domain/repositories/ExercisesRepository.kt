package com.example.workoutapp.domain.repositories

import com.example.workoutapp.domain.models.Exercise
import kotlinx.coroutines.flow.Flow


interface ExercisesRepository {
    suspend fun getExercises(): List<Exercise>

    suspend fun observeExercises(): Flow<List<Exercise>>
}