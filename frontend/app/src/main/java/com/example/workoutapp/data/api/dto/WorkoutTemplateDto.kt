package com.example.workoutapp.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Data transfer object for a workout template consisting of id, name
 * and a list of exercises
 */
@Serializable
data class WorkoutTemplateDto(
    val templateId: String,
    var name: String,
    val exercises: List<TemplateExerciseDto>
)

/**
 * Data transfer object for a template exercise, containing all the data
 * from an exercise plus a list of set(s)
 */
@Serializable
data class TemplateExerciseDto(
    val exercise: ExerciseDto,
    val sets: List<SetDto>
)