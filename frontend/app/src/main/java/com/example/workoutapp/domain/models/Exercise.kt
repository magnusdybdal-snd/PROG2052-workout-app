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
    val gifUrl: String?, // Remote URL for detail page
    val instructions: List<String>
) {
    // Helper function to get local thumbnail path
    fun getLocalThumbnailPath(): String {
        return "file:///android_asset/exercise_thumbs/${exerciseId}.webp"
    }
}