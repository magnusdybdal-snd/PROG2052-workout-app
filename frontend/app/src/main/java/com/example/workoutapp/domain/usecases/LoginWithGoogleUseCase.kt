package com.example.workoutapp.domain.usecases

import com.example.workoutapp.domain.models.AuthResponse
import com.example.workoutapp.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(code: String): AuthResponse {
        return repository.loginWithGoogle(code)
    }
}