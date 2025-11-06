package com.example.workoutapp.domain.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable

@Serializable
data class Set(
    var rep: Int,
    var kg: Double,
    var typeSet: Int
) { // Computed value, not stored but computed at access time
    val volume: Int
        get() = (rep * kg).toInt()
}