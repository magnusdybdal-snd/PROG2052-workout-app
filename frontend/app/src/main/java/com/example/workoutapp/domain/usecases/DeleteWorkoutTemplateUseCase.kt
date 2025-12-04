package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import javax.inject.Inject

/**
 * Use case for deleting a workout template.
 *
 * Encapsulates the business logic for template deletion, delegating
 * to the repository which handles removal from both local and remote storage.
 *
 * @property repository The workout template repository for data access
 */
class DeleteWorkoutTemplateUseCase @Inject constructor(
    private val repository: WorkoutTemplateRepository
) {
    /**
     * Deletes a workout template.
     *
     * The template is removed from local Room database and deleted
     * from the backend API.
     *
     * @param workoutTemplate The template to delete
     */
    suspend operator fun invoke(workoutTemplate: WorkoutTemplate) {
        repository.deleteWorkoutTemplate(workoutTemplate)
    }
}
