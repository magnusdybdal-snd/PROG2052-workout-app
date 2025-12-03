package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.NewTemplate
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import javax.inject.Inject

/**
 * Use case for creating a new workout template.
 *
 * Encapsulates the business logic for template creation, delegating
 * to the repository which handles persistence to both local and remote storage.
 *
 * @property repository The workout template repository for data access
 */
class PostWorkoutTemplateUseCase @Inject constructor(
    private val repository: WorkoutTemplateRepository
) {
    /**
     * Creates a new workout template.
     *
     * The template is saved to local Room database first, then synced
     * to the backend API.
     *
     * @param newTemplate The template to create
     */
    suspend operator fun invoke(newTemplate: NewTemplate) {
        repository.postWorkoutTemplate(newTemplate)
    }
}