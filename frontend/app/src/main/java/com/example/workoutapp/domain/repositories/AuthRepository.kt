package com.example.workoutapp.domain.repositories

import com.example.workoutapp.domain.models.AuthResponse

/**
 * Repository interface for authentication operations.
 *
 * Handles user authentication via Google OAuth, including exchanging the
 * authorization code for a JWT token from the backend.
 *
 * The domain layer depends only on this abstraction, while the actual
 * implementation is provided in the data layer.
 */
interface AuthRepository {
    /**
     * Authenticates a user with Google OAuth.
     *
     * Exchanges the Google authorization code for user credentials by calling
     * the backend API. The backend validates the code with Google, retrieves
     * or creates the user account, and returns a JWT token for subsequent requests.
     *
     * @param code Authorization code obtained from Google Sign-In
     * @return AuthResponse containing JWT token, user ID, and display name
     * @throws Exception if authentication fails or network error occurs
     */
    suspend fun loginWithGoogle(code: String): AuthResponse
}