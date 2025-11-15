package com.example.workoutapp.domain.repositories

import com.example.workoutapp.domain.models.Exercise
import kotlinx.coroutines.flow.StateFlow


interface ExercisesRepository {
    suspend fun getExercises(): List<Exercise>

    fun observeExercises(): StateFlow<List<Exercise>>
}