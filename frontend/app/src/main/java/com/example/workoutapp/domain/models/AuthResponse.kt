package com.example.workoutapp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val userId: String
)