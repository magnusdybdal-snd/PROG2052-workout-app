package com.example.workoutapp.domain.repositories

import com.example.workoutapp.data.api.dto.HistoryWorkoutDto
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.Session
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing and syncing past workouts.
 *
 * Defines the contract for fetching past workouts from any data source
 * (e.g., remote API, local database, or test doubles).
 *
 * The domain layer depends only on this abstraction, while the actual
 * implementation is provided in the data layer.
 */
interface HistoryWorkoutRepository {

    /**
     * Fetches the latest workouts from the API and updates local Room storage.
     * Returns the updated list after syncing.
     */
    suspend fun getHistoryWorkouts(): List<HistoryWorkout>

    /**
     * Observes all stored workouts in local Room database.
     * Returns a Flow so the UI can automatically update when data changes.
     */
    fun observeHistoryWorkouts(): Flow<List<HistoryWorkout>>

    /**
     * Posts a completed workout to the API and saves it locally.
     */
    suspend fun postHistoryWorkout(session: Session)
}