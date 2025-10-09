package com.example.workoutapp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Set(
    var rep: Int,
    var kg: Int,
    var typeSet: Int
) { // Computed value, not stored but computed at access time
    val volume: Int
        get() = rep * kg
}