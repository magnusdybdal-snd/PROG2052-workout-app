package com.example.workoutapp.data.repositories

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.domain.models.AuthResponse
import com.example.workoutapp.domain.repositories.AuthRepository
import javax.inject.Inject

/**
 * Implementation of [AuthRepository] that handles user authentication.
 *
 * Delegates Google OAuth authentication to the backend API, which validates
 * the authorization code with Google and returns app credentials (JWT token).
 *
 * @property api API service for backend authentication endpoints
 */
class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService
) : AuthRepository {

    /**
     * Authenticates a user via Google OAuth.
     *
     * Sends the Google authorization code to the backend, which exchanges it
     * for user credentials. The backend creates or retrieves the user account
     * and issues a JWT token for subsequent API requests.
     *
     * @param code Authorization code from Google Sign-In
     * @return AuthResponse containing JWT token, user ID, and display name
     * @throws Exception if authentication fails or network error occurs
     */
    override suspend fun loginWithGoogle(code: String) : AuthResponse {
        return api.loginWithGoogle(code)
    }
}