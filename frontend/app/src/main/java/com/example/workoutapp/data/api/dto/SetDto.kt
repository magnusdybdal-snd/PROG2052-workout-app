package com.example.workoutapp.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data transfer object for an individual set within an exercise.
 *
 * Represents a single set with reps, weight, and type information.
 * Used in both workout templates (planned sets) and history (completed sets).
 *
 * @property rep Number of repetitions for this set
 * @property kg Weight in kilograms for this set
 * @property typeSet Type of set (0 = normal, 1 = warmup, 2 = drop set, etc.)
 */
@Serializable
data class SetDto(
    val rep: Int,
    val kg: Double,
    val typeSet: Int
)