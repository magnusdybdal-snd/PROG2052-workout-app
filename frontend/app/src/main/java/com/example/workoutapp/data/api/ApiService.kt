package com.example.workoutapp.data.api

import com.example.workoutapp.data.api.dto.ExerciseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class ApiService @Inject constructor(
    private val client: HttpClient,
    private val baseUrl: String
) {

    suspend fun getExercises(): List<ExerciseDto> {
        return client.get("$baseUrl/exercises").body()
    }

    // More API calls like getWorkoutTemplates will be added here
}
