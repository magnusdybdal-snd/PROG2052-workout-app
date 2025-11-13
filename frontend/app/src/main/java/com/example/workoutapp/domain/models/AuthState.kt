package com.example.workoutapp.domain.models

/**
 * Represents the authentication STATE of the user
 */
sealed class AuthState {
    object Loading: AuthState()
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
}