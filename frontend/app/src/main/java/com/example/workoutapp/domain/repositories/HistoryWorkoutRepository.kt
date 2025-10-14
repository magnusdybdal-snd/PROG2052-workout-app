package com.example.workoutapp.domain.repositories

import com.example.workoutapp.data.api.dto.HistoryWorkoutDto
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.Session
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing past workouts.
 *
 * Defines the contract for fetching past workouts from any data source
 * (e.g., remote API, local database, or test doubles).
 *
 * The domain layer depends only on this abstraction, while the actual
 * implementation is provided in the data layer.
 */
interface HistoryWorkoutRepository {
    suspend fun getHistoryWorkouts(): List<HistoryWorkout>
    suspend fun observeHistoryWorkouts(): Flow<List<HistoryWorkout>>
    suspend fun postHistoryWorkout(session: Session)
}