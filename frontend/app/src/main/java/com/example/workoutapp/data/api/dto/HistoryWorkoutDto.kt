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
 * Data transfer object for a past workout / completed workout with
 * its data members
 */
@Serializable
data class HistoryWorkoutDto(
    @SerialName("sessionId")
    val historyWorkoutId: String,
    val name: String,
    val exercises: List<WorkoutExerciseDto>,
    val duration: String, //"01:10:00"
    val date: String, //"2025-10-01"
    val note: String? = ""
)

/**
 * Data transfer object for a workout exercise, containing exercise ID,
 * name, and a list of set(s)
 * Note: No longer includes full exercise details (removed ?include=exercises)
 */
@Serializable
data class WorkoutExerciseDto(
    val exerciseId: String,
    val name: String,
    val sets: List<SetDto>
)