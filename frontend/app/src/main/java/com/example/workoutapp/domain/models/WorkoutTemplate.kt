package com.example.workoutapp.domain.models

import java.time.LocalDateTime

/**
 * Data class for a WorkoutTemplate.
 * Contains a list of TemplateExercise, not to be mixed with WorkoutExercise or Exercise
 *
 * @see TemplateExercise
 */
data class WorkoutTemplate(
    val templateId: String,
    val name: String,
    val createdAt: LocalDateTime,
    val exercises: List<TemplateExercise>
)

/**
 * Data class for an exercise belonging to a template.
 * This is different from an exercise belonging to a workout (WorkoutExercise)
 * and different from an exercise in the library (Exercise)
 *
 * @see Exercise
 */
data class TemplateExercise(
    val exerciseId: String,
    val name: String,
    val sets: List<Set>
)