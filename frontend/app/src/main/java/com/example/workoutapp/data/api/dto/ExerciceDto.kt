package com.example.workoutapp.data.api.dto

import com.example.workoutapp.domain.models.Exercise
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data transfer object for a exercise (in library) with its data members
 */
@Serializable
data class ExerciseDto(
    val exerciseId: String,
    val name: String,
    val targetMuscles: List<String>,
    val bodyParts: List<String>,
    val equipments: List<String>,
    val secondaryMuscles: List<String>,
    val gifUrl: String,
    val instructions: List<String>
)