package com.example.workoutapp.domain.models

import kotlinx.serialization.Serializable

/**
 * Domain model representing the authentication response from the backend API.
 *
 * Returned after successful Google OAuth authentication. Contains the JWT token
 * that must be included in subsequent API requests and user profile information.
 *
 * @property token JWT authentication token for API requests
 * @property userId Unique identifier for the authenticated user
 * @property name Display name of the authenticated user (from Google profile)
 */
@Serializable
data class AuthResponse(
    val token: String,
    val userId: String,
    val name: String
)