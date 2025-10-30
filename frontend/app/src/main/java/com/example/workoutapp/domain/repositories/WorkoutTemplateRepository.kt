package com.example.workoutapp.domain.repositories

import com.example.workoutapp.data.api.dto.WorkoutTemplateDto
import com.example.workoutapp.domain.models.NewTemplate
import com.example.workoutapp.domain.models.WorkoutTemplate
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing workout templates.
 *
 * Defines the contract for fetching workout templates from any data source
 * (e.g., remote API, local database, or test doubles).
 *
 * The domain layer depends only on this abstraction, while the actual
 * implementation is provided in the data layer.
 */
interface WorkoutTemplateRepository {
    suspend fun getWorkoutTemplates(): List<WorkoutTemplate>
    suspend fun postWorkoutTemplate(newTemplate: NewTemplate)
    suspend fun deleteWorkoutTemplate(template: WorkoutTemplate)
    suspend fun syncDeletedTemplates()
    fun observeTemplates(): Flow<List<WorkoutTemplate>>
}