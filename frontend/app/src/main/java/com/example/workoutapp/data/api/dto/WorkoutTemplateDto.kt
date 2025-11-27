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

/**
 * Data transfer object for example templates (flat structure from backend)
 */
@Serializable
data class ExampleTemplateDto(
    val templateId: String,
    val name: String,
    val exercises: List<ExampleTemplateExerciseDto>
)

/**
 * Data transfer object for example template exercise (flat structure without nested exercise object)
 */
@Serializable
data class ExampleTemplateExerciseDto(
    val exerciseId: String,
    val name: String,
    val sets: List<SetDto>
)