package com.example.workoutapp.data.api

import com.example.workoutapp.data.api.dto.ExerciseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ApiService(private val client: HttpClient) {

    suspend fun getExercises(): List<ExerciseDto> {
        return client.get("http://10.212.168.186:8080/api/v1/exercises?limit=10").body()
    }
}
