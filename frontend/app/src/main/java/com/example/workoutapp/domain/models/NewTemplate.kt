package com.example.workoutapp.domain.models

import kotlinx.serialization.Serializable

/**
 * Data transfer object for creating a new workout template.
 *
 * Used when sending template creation requests to the backend API.
 * Differs from [WorkoutTemplate] by potentially omitting server-generated
 * fields like timestamps.
 *
 * @property templateId Client-generated unique identifier
 * @property name User-defined name for the template
 * @property exercises List of exercises to include in the new template
 * @see WorkoutTemplate
 */
@Serializable
data class NewTemplate (
    val templateId: String,
    var name: String,
    val exercises: List<NewTemplateExercise>
)

/**
 * Exercise configuration for a new template being created.
 *
 * @property exerciseId Reference to the exercise in the library
 * @property name Display name of the exercise
 * @property sets Mutable list of planned set configurations
 */
@Serializable
data class NewTemplateExercise(
    val exerciseId: String,
    val name: String,
    val sets: MutableList<Set>
)