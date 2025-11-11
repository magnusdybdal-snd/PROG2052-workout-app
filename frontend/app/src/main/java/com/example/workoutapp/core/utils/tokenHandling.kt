package com.example.workoutapp.core.utils

import com.auth0.android.jwt.JWT

const val leeway: Long = 10

fun isTokenExpired(token: String?): Boolean {
    if (token.isNullOrBlank()) {
        return true
    }
    return try {
        val jwt = JWT(token)
        jwt.isExpired(leeway)
    } catch(e: Exception) {
        return true
    }
}