package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import javax.inject.Inject

/**
 * Use case for editing an existing workout template.
 *
 * Encapsulates the business logic for template modification, delegating
 * to the repository which handles persistence to both local and remote storage.
 *
 * @property repository The workout template repository for data access
 */
class EditWorkoutTemplateUseCase @Inject constructor(
    private val repository: WorkoutTemplateRepository
) {
    /**
     * Updates an existing workout template.
     *
     * The changes are saved to local Room database first, then synced
     * to the backend API.
     *
     * @param workoutTemplate The template with updated values
     */
    suspend operator fun invoke(workoutTemplate: WorkoutTemplate) {
        repository.editWorkoutTemplate(workoutTemplate)
    }
}
