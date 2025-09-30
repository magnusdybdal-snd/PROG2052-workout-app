package com.example.workoutapp.domain.repositories

import com.example.workoutapp.domain.models.Exercise


interface ExercisesRepository {
    suspend fun getExercises(): List<Exercise>
}