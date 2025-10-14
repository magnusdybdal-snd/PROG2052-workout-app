package com.example.workoutapp.data.api

import com.example.workoutapp.data.api.dto.ExerciseDto
import com.example.workoutapp.data.api.dto.HistoryWorkoutDto
import com.example.workoutapp.data.api.dto.WorkoutTemplateDto
import com.example.workoutapp.domain.models.Session
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

/**
 * Main API service class.
 * Contains all functions for calls to the API that is used trough the app.
 */
class ApiService @Inject constructor(
    private val client: HttpClient,
    private val baseUrl: String
) {

    /**
     * Gets all the exercises (library) from the backend API
     */
    suspend fun getExercises(): List<ExerciseDto> {
        return client.get("$baseUrl/exercises").body()
    }

    /**
     * Gets all the WorkoutTemplates from one user from the backend API.
     * (MVP currently pulls one specific template)
     */
    suspend fun getWorkoutTemplates(): List<WorkoutTemplateDto> {
        // TODO: URL needs to be changed for one with param to fetch for logged in user
        return client.get("$baseUrl/templates?include=exercises").body()
    }

    suspend fun getHistoryWorkouts(): List<HistoryWorkoutDto> {
        // TODO: URL needs to be changed for one with param to fetch for logged in user
        return client.get("$baseUrl/sessions?include=exercises").body()
    }

    suspend fun postHistoryWorkout(session: Session) {
        client.post("$baseUrl/sessions") {
            contentType(ContentType.Application.Json)
            setBody(session)
        }
    }

    suspend fun postWorkoutTemplate(workoutTemplateDto: WorkoutTemplateDto) {
        client.post("$baseUrl/templates") {
            contentType(ContentType.Application.Json)
            setBody(workoutTemplateDto)
        }
    }
    // More API calls like getWorkoutTemplates will be added here
}
