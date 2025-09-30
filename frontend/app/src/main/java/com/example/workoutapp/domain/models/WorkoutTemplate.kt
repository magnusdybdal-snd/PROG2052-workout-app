package com.example.workoutapp.domain.models

/**
 * Data class for a WorkoutTemplate.
 * Contains a list of TemplateExercise, not to be mixed with WorkoutExercise or Exercise
 *
 * @see TemplateExercise
 */
data class WorkoutTemplate(
    val templateId: String,
    val name: String,
    val exercises: List<TemplateExercise>
)