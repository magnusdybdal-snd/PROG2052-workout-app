package com.example.workoutapp.data.api.dto

import com.example.workoutapp.domain.models.Exercise
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.Set
import com.example.workoutapp.domain.models.WorkoutExercise
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import kotlin.time.Duration
import kotlin.time.toJavaDuration

/**
 * Data transfer object for a completed workout session.
 *
 * Represents a past workout with all exercises performed and sets completed.
 * Received from the backend and converted to domain HistoryWorkout model.
 *
 * @property historyWorkoutId Unique identifier for this workout session
 * @property name Name of the workout (often based on the template used)
 * @property exercises List of exercises performed with completed sets
 * @property duration Total workout duration in HH:mm:ss format (e.g., "01:10:00")
 * @property date Date when the workout was completed in ISO format (e.g., "2025-10-01")
 * @property note Optional user notes about the workout session
 */
@Serializable
data class HistoryWorkoutDto(
    @SerialName("sessionId")
    val historyWorkoutId: String,
    val name: String,
    val exercises: List<WorkoutExerciseDto>,
    val duration: String,
    val date: String,
    val note: String? = ""
)

/**
 * Data transfer object for an exercise within a completed workout.
 *
 * Contains the exercise ID, name, and all sets that were completed.
 * Uses a flat structure (ID + name only) rather than full exercise details
 * for efficient serialization.
 *
 * @property exerciseId Unique identifier matching the exercise library
 * @property name Display name of the exercise
 * @property sets List of completed sets with reps and weight
 */
@Serializable
data class WorkoutExerciseDto(
    val exerciseId: String,
    val name: String,
    val sets: List<SetDto>
)