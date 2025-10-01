package com.example.workoutapp.data.api.dto

import kotlinx.serialization.Serializable

/**
 * Data transfer object for a past workout / completed workout with
 * its data members
 */
@Serializable
data class HistoryWorkoutDto(
    val historyWorkoutId: String,
    val name: String,
    val date: String,
    val length: String,
    val exercises: List<WorkoutExercise>,
    val note: String
)

/**
 * Data transfer object for a template exercise, containing all the data
 * from en exercise plus a list of set(s)
 * TODO: Add notes later
 */
@Serializable
data class WorkoutExercise(
    val exercise: ExerciseDto,
    val sets: List<SetDto>
)