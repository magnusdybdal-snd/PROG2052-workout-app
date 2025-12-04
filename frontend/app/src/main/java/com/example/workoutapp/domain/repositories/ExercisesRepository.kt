package com.example.workoutapp.domain.repositories

import com.example.workoutapp.domain.models.Exercise
import kotlinx.coroutines.flow.StateFlow

/**
 * Repository interface for accessing the exercise library.
 *
 * The exercise library is pre-loaded from assets and contains ~1500 exercises
 * with details about target muscles, equipment, and instructions.
 *
 * The domain layer depends only on this abstraction, while the actual
 * implementation is provided in the data layer.
 */
interface ExercisesRepository {
    /**
     * Fetches the complete list of exercises from Room
     *
     * @return List of all available exercises
     */
    suspend fun getExercises(): List<Exercise>

    /**
     * Observes all exercises as a reactive StateFlow.
     *
     * The UI can collect this flow to automatically update when exercises
     * change
     *
     * @return StateFlow that emits the current list of exercises
     */
    fun observeExercises(): StateFlow<List<Exercise>>
}