package com.example.workoutapp.domain.models

import com.example.workoutapp.data.serializers.DurationSerializer
import com.example.workoutapp.data.serializers.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.LocalDate

/**
 * Domain model representing a completed workout session saved to history.
 *
 * History workouts contain the actual performance data from a completed workout,
 * including the exercises performed, sets completed with actual weight/reps,
 * total duration, and optional notes.
 *
 * @property id Unique identifier for this history workout
 * @property name Name of the workout (typically copied from the template used)
 * @property duration Total time spent on the workout from start to finish
 * @property date Date when the workout was performed
 * @property exercises List of exercises completed with actual performance data
 * @property note Optional user note about the workout session
 */
@Serializable
data class HistoryWorkout(
    val id: String,
    val name: String,
    @Serializable(with = DurationSerializer::class)
    val duration: Duration,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val exercises: List<WorkoutExercise>,
    val note: String = ""
) {
    /**
     * Computed total volume (weight × reps) across all exercises in this workout.
     * Calculated on-demand and not persisted to storage.
     */
    val totalVolume: Int
        get() = exercises.sumOf { it.volume }
}

/**
 * Domain model representing an exercise completed within a workout session.
 *
 * Contains actual performance data (sets with weight and reps achieved) rather than
 * planned configuration. Differs from [TemplateExercise] which defines the plan,
 * and [Exercise] which is the library reference.
 *
 * @property exerciseId Reference to the exercise in the library
 * @property name Display name of the exercise
 * @property sets List of completed sets with actual performance data
 * @see Exercise
 * @see TemplateExercise
 */
@Serializable
data class WorkoutExercise(
    val exerciseId: String,
    val name: String,
    val sets: List<Set>
) {
    /**
     * Computed total volume (weight × reps) for this exercise.
     * Calculated on-demand and not persisted to storage.
     */
    val volume: Int
        get() = sets.sumOf { it.volume }
}
