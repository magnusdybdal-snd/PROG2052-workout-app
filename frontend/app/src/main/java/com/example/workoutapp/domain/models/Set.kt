package com.example.workoutapp.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable

/**
 * Domain model representing a single set of an exercise.
 *
 * Used in both templates (planned sets) and history workouts (completed sets).
 * Contains the target or actual repetitions, weight, and set type.
 *
 * @property rep Number of repetitions (target for templates, actual for history)
 * @property kg Weight in kilograms (target for templates, actual for history)
 * @property typeSet Type/category of set (e.g., working set, warmup, drop set)
 */
@Serializable
data class Set(
    var rep: Int,
    var kg: Double,
    var typeSet: Int
) {
    /**
     * Computed volume (load) for this set, calculated as repetitions × weight.
     * Calculated on-demand and not persisted to storage.
     */
    val volume: Int
        get() = (rep * kg).toInt()
}