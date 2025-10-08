package com.example.workoutapp.domain.models

import java.time.Duration
import java.time.LocalDate

/**
 * Data class for a HistoryWorkout (completed / past workout)
 * Contains a list of WorkoutExercise, not to be mixed with TemplateExercise
 */
data class HistoryWorkout(
    val historyWorkoutId: String,
    val name: String,
    val date: LocalDate,
    val duration: Duration,
    val exercises: List<WorkoutExercise>,
    val note: String
) { // Computed value, not stored but computed at access time
    val totalVolume: Int
        get() = exercises.sumOf { it.volume }
}

/**
 * Data class that represents a Exercise in a completed / past workout.
 * Not to be mixed with TemplateExercise.
 *
 * @see Exercise
 */
data class WorkoutExercise(
    val exercise: Exercise,
    val sets: List<Set>
) { // Computed value, not stored but computed at access time
    val volume: Int
        get() = sets.sumOf { it.volume }
}