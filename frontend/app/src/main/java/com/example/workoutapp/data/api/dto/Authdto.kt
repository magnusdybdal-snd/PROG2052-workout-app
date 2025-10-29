package com.example.workoutapp.data.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class Authdto(
    val token: String,
    val userId: String
)