package com.example.workoutapp.data.api.dto

import kotlinx.serialization.Serializable

/**
 * Data transfer object for an exercise from the library.
 *
 * Represents an exercise with all metadata needed for display and filtering.
 * Received from the backend API and converted to domain Exercise model.
 *
 * @property exerciseId Unique identifier for the exercise
 * @property name Display name of the exercise (e.g., "Barbell Bench Press")
 * @property targetMuscles Primary muscle groups worked by this exercise
 * @property bodyParts Body regions involved (e.g., ["upper arms", "chest"])
 * @property equipments Required equipment (e.g., ["barbell", "bench"])
 * @property secondaryMuscles Additional muscles engaged during the exercise
 * @property gifUrl URL to animated GIF demonstrating proper form
 * @property instructions Step-by-step instructions for performing the exercise
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