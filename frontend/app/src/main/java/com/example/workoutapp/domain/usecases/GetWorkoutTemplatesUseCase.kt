package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving workout templates (local first)
 *
 * Observes local database changes as a flow (auto updates UI)
 * Optionally triggers a background sync with the backend API
 */
class GetWorkoutTemplatesUseCase @Inject constructor(
    private val repository: WorkoutTemplateRepository
) {
    /**
     * Observe the local database for template changes (reactive)
     */
    operator fun invoke(): Flow<List<WorkoutTemplate>> {
        return repository.observeTemplates()
    }

    /**
     * Perform a manual sync from remote API to local database
     */
    suspend fun syncNow(): List<WorkoutTemplate> {
        return repository.getWorkoutTemplates()
    }
}
