package com.example.workoutapp.data.repositories

import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.domain.models.AuthResponse
import com.example.workoutapp.domain.repositories.AuthRepository
import javax.inject.Inject

// Implement the authentication request
class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService) : AuthRepository {

    override suspend fun loginWithGoogle(code: String) : AuthResponse {
        return api.loginWithGoogle(code)
    }
}