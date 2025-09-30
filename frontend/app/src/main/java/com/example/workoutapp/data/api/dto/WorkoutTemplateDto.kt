package com.example.workoutapp.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data transfer object for a workout template consisting of id, name
 * and a list of exercises
 */
@Serializable
data class WorkoutTemplateDto(
    val templateId: String,
    val name: String,
    val exercises: List<TemplateExerciseDto>
)

/**
 * Data transfer object for a template exercise, containing all the data
 * from an exercise plus a list of set(s)
 */
@Serializable
data class TemplateExerciseDto(
    val exercise: ExerciseDto,
    val set: List<SetDto>
)

/**
 * Data transfer object for a set with rep, kg and type of set
 */
@Serializable
data class SetDto(
    val rep: Int,
    val kg: Int,
    @SerialName("TypeSet") // TODO: Endre i backend til camelCase
    val typeSet: Int
)