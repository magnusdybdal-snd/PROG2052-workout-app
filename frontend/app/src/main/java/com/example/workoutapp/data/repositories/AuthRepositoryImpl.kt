package com.example.workoutapp.data.repositories

import com.example.workoutapp.data.api.ApiService
import javax.inject.Inject

// Implement the authentication request
class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService) {

    override suspend fun loginWithGoogle(code: String) : AuthRepository {
        return api.loginWithGoogle(code)
    }
}