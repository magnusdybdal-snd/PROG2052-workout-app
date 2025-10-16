package com.example.workoutapp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val exerciseId: String,
    val name: String,
    val targetMuscles: List<String>,
    val bodyParts: List<String>,
    val equipments: List<String>,
    val secondaryMuscles: List<String>,
    val gifUrl: String?,
    val instructions: List<String>
)