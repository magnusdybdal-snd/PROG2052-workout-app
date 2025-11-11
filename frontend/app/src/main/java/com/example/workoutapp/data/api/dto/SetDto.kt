package com.example.workoutapp.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data transfer object for a set with rep, kg and type of set
 */
@Serializable
data class SetDto(
    val rep: Int,
    val kg: Double,
    val typeSet: Int
)