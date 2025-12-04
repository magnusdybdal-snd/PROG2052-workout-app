package com.example.workoutapp.domain.models

import kotlinx.serialization.Serializable

/**
 * Domain model representing an exercise from the pre-loaded exercise library.
 *
 * The exercise library contains ~1500 exercises with detailed information including
 * target muscles, equipment needed, and step-by-step instructions.
 *
 * @property exerciseId Unique identifier for the exercise
 * @property name Display name of the exercise
 * @property targetMuscles List of primary muscle groups targeted by this exercise
 * @property bodyParts List of body parts involved in this exercise
 * @property equipments List of equipment required (e.g., "barbell", "dumbbells", "bodyweight")
 * @property secondaryMuscles List of secondary muscle groups engaged during the exercise
 * @property gifUrl Remote URL to animated GIF demonstration (nullable for exercises without animations)
 * @property instructions Step-by-step instructions for performing the exercise correctly
 */
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
) {
    /**
     * Returns the local file path for the exercise thumbnail image.
     *
     * Thumbnails are stored in the assets folder as WebP images for optimal loading
     * performance in list views.
     *
     * @return File URI pointing to the local thumbnail asset
     */
    fun getLocalThumbnailPath(): String {
        return "file:///android_asset/exercise_thumbs/${exerciseId}.webp"
    }
}