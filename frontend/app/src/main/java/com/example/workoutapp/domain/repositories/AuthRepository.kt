package com.example.workoutapp.domain.repositories

import com.example.workoutapp.domain.models.AuthResponse

interface AuthRepository {
    suspend fun loginWithGoogle(code: String): AuthResponse
}