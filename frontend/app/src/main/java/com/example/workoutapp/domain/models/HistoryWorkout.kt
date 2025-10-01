package com.example.workoutapp.domain.models

import kotlinx.serialization.SerialName

/**
 * Data class for a HistoryWorkout (completed / past workout)
 * Contains a list of WorkoutExercise, not to be mixed with TemplateExercise
 */
data class HistoryWorkout(
    val historyWorkoutId: String,
    val name: String,
    val date: String,
    val duration: String,
    // TODO: endre i backend til "exercises"
    @SerialName("exercise")
    val exercises: List<WorkoutExercise>,
    val note: String
) {
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
) {
    val volume: Int
        get() = sets.sumOf { it.volume }
}