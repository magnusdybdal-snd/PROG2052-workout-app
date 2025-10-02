package com.example.workoutapp.domain.models

data class Set(
    val rep: Int,
    val kg: Int,
    val typeSet: Int
) { // Computed value, not stored but computed at access time
    val volume: Int
        get() = rep * kg
}