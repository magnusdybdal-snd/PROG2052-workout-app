package com.example.workoutapp.domain.repositories

import com.example.workoutapp.domain.models.NewTemplate
import com.example.workoutapp.domain.models.WorkoutTemplate
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for accessing workout templates.
 *
 * Workout templates are user-created workout plans that define exercise selection
 * and set configurations. Templates can be created, edited, deleted, and used as
 * blueprints when starting a workout session.
 *
 * The domain layer depends only on this abstraction, while the actual
 * implementation is provided in the data layer.
 */
interface WorkoutTemplateRepository {
    /**
     * Fetches all user-created workout templates.
     *
     * Synchronizes with the backend API and updates local Room storage,
     * then returns the updated list.
     *
     * @return List of all workout templates for the current user
     */
    suspend fun getWorkoutTemplates(): List<WorkoutTemplate>

    /**
     * Retrieves example/default workout templates as a Flow.
     *
     * Example templates serve as starting points for new users or inspiration
     * for experienced users.
     *
     * @return Flow emitting the list of example templates
     */
    suspend fun getExampleTemplates(): Flow<List<WorkoutTemplate>>

    /**
     * Synchronizes example templates from the backend API to local storage.
     */
    suspend fun syncExampleTemplates()

    /**
     * Creates a new workout template.
     *
     * Saves to local Room database first, then pushes to the backend API.
     * If the API request succeeds, the template is marked as synced.
     *
     * @param newTemplate The template to create
     */
    suspend fun postWorkoutTemplate(newTemplate: NewTemplate)

    /**
     * Deletes an existing workout template.
     *
     * Removes from local Room database and deletes from the backend API.
     *
     * @param workoutTemplate The template to delete
     */
    suspend fun deleteWorkoutTemplate(workoutTemplate: WorkoutTemplate)

    /**
     * Updates an existing workout template.
     *
     * Updates local Room database first, then pushes changes to the backend API.
     *
     * @param workoutTemplate The template with updated values
     */
    suspend fun editWorkoutTemplate(workoutTemplate: WorkoutTemplate)

    /**
     * Synchronizes templates that were deleted locally with the backend API.
     *
     * Ensures deletion operations are propagated to the server.
     */
    suspend fun syncDeletedTemplates()

    /**
     * Observes all workout templates as a reactive Flow.
     *
     * The UI can collect this flow to automatically update when templates
     * are created, edited, or deleted.
     *
     * @return Flow emitting the current list of templates
     */
    fun observeTemplates(): Flow<List<WorkoutTemplate>>
}