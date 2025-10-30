package com.example.workoutapp.domain.models

import com.example.workoutapp.data.serializers.DurationSerializer
import com.example.workoutapp.data.serializers.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.LocalDate

/**
 * Data class for a HistoryWorkout (completed / past workout)
 * Contains a list of WorkoutExercise, not to be mixed with TemplateExercise
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
@Serializable
data class WorkoutExercise(
    val exerciseId: String,
    val name: String,
    val sets: List<Set>
) { // Computed value, not stored but computed at access time
    val volume: Int
        get() = sets.sumOf { it.volume }
}
