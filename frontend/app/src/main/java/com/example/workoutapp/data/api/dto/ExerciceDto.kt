package com.example.workoutapp.data.api.dto

import com.example.workoutapp.domain.models.Exercise
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDto(
    @SerialName("exerciseId") val id: String,
    val name: String,
    val targetMuscles: List<String>,
    val bodyParts: List<String>,
    val equipments: List<String>,
    val secondaryMuscles: List<String>,
    val gifUrl: String,
    val instructions: List<String>
)

fun ExerciseDto.toDomain(): Exercise {
    return Exercise(
        name = name,
        equipments = equipments,
        targetMuscles = targetMuscles,
        bodyParts = bodyParts,
        secondaryMuscles = secondaryMuscles,
        gifUrl = gifUrl,
        instructions = instructions,
    )
}