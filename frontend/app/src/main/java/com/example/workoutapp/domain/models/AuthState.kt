package com.example.workoutapp.domain.models

/**
 * Sealed class representing the authentication state of the user.
 *
 * Used to drive UI state and navigation decisions based on whether the user
 * is logged in, logging in, or logged out.
 */
sealed class AuthState {
    /**
     * Authentication status is being determined (e.g., checking stored token validity).
     */
    object Loading: AuthState()

    /**
     * User is authenticated with a valid JWT token.
     */
    object Authenticated: AuthState()

    /**
     * User is not authenticated or token has expired.
     */
    object Unauthenticated: AuthState()
}