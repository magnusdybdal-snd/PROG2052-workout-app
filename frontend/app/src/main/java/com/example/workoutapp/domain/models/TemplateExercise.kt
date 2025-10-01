package com.example.workoutapp.domain.models

/**
 * Data class for an exercise belonging to a template.
 * This is different from an exercise belonging to a workout (WorkoutExercise)
 * and different from an exercise in the library (Exercise)
 *
 * @see Exercise
 */
data class TemplateExercise(
    val exercise: Exercise,
    val sets: List<Set>
)