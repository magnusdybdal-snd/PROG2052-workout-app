package com.example.workoutapp.domain.models

data class Set(
    val rep: Int,
    val kg: Int,
    val typeSet: Int
) {
    val volume: Int
        get() = rep * kg
}