package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.AuthResponse
import com.example.workoutapp.domain.repositories.AuthRepository
import javax.inject.Inject

/**
 * Use case for authenticating users via Google OAuth.
 *
 * Handles the authentication flow by exchanging the Google authorization code
 * for app credentials (JWT token) through the backend API.
 *
 * @property repository The authentication repository for data access
 */
class LoginWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    /**
     * Authenticates a user with their Google authorization code.
     *
     * The backend validates the code with Google, retrieves or creates
     * the user account, and returns a JWT token for subsequent API requests.
     *
     * @param code Authorization code obtained from Google Sign-In flow
     * @return AuthResponse containing JWT token, user ID, and display name
     * @throws Exception if authentication fails or network error occurs
     */
    suspend operator fun invoke(code: String): AuthResponse {
        return repository.loginWithGoogle(code)
    }
}