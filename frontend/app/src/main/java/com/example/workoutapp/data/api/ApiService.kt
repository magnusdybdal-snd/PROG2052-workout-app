package com.example.workoutapp.data.api

import com.example.workoutapp.data.api.dto.ExerciseDto
import com.example.workoutapp.data.api.dto.HistoryWorkoutDto
import com.example.workoutapp.data.api.dto.WorkoutTemplateDto
import com.example.workoutapp.data.database.UserPreferences
import com.example.workoutapp.domain.models.AuthResponse
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.domain.models.NewTemplate
import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.models.WorkoutTemplate
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

/**
 * Main API service class for backend communication.
 *
 * Provides a centralized interface for all HTTP requests to the backend API.
 * Uses Ktor HttpClient for network operations and is injected via Hilt.
 *
 * @property client Ktor HTTP client for making network requests
 * @property baseUrl Base URL of the backend API
 */
class ApiService @Inject constructor(
    private val client: HttpClient,
    private val baseUrl: String,
    private val preferences: UserPreferences
) {
    private suspend fun getAuthHeader(): String? {
        return preferences.token.firstOrNull()
    }
    /**
     *  /POST
     *  Request jwt token from the backend.
     *  used for user authenticated data
    * */
    suspend fun loginWithGoogle(code: String): AuthResponse {
        return client.post("$baseUrl/auth/google") {
            contentType(ContentType.Application.Json)
            setBody(mapOf("code" to code))
        }.body()
    }

    //===========================================
    // EXERCISE LIBRARY ENDPOINTS
    //===========================================

    /**
     * Fetches the exercise library from the backend.
     *
     * Returns a list of available exercises that users can add to their templates
     * and workouts. Currently limited to 30 exercises for MVP.
     *
     * @return List of exercise DTOs containing exercise metadata
     */
    suspend fun getExercises(): List<ExerciseDto> {
        return client.get("$baseUrl/exercises?limit=30").body()
    }

    //===========================================
    // WORKOUT TEMPLATE ENDPOINTS (CRUD)
    //===========================================

    /**
     * Fetches all workout templates for the current user.
     *
     * Retrieves templates with their associated exercises in a single request.
     * Templates are used as blueprints for starting new workout sessions.
     *
     * @return List of workout template DTOs with nested exercise data
     * TODO: Add user authentication to fetch only current user's templates
     */
    suspend fun getWorkoutTemplates(): List<WorkoutTemplateDto> {
        val token = getAuthHeader()
        return client.get("$baseUrl/templates?include=exercises") {
            token?.let {
                headers {
                    append("Authorization", "Bearer $it")
                }
            }
        }.body()
    }

    /**
     * Creates a new workout template on the backend.
     *
     * Uploads a locally-created template to the backend for cloud storage
     * and synchronization across devices.
     *
     * @param newTemplate The template to create (includes exercises and sets)
     */
    suspend fun postWorkoutTemplate(newTemplate: NewTemplate) {
        val token = getAuthHeader()
        client.post("$baseUrl/templates") {
            contentType(ContentType.Application.Json)
            setBody(newTemplate)
            token?.let {
                headers {
                    append("Authorization", "Bearer $it")
                }
            }
        }
    }

    /**
     * Updates an existing workout template on the backend.
     *
     * Syncs local changes to the template (name, exercises, sets) with the backend.
     *
     * @param workoutTemplate The template with updated data
     */
    suspend fun editWorkoutTemplate(workoutTemplate: WorkoutTemplate) {
        val token = getAuthHeader()
        client.put("$baseUrl/templates/${workoutTemplate.templateId}") {
            contentType(ContentType.Application.Json)
            setBody(workoutTemplate)
            token?.let {
                headers {
                    append("Authorization", "Bearer $it")
                }
            }
        }
    }

    /**
     * Deletes a workout template from the backend.
     *
     * Permanently removes the template from cloud storage. This is called after
     * a local soft-delete and user confirmation. If this fails, the delete will
     * be retried on the next sync.
     *
     * @param templateId UUID of the template to delete
     */
    suspend fun deleteWorkoutTemplate(templateId: String) {
        val token = getAuthHeader()
        client.delete("$baseUrl/templates/$templateId") {
            token?.let {
                headers {
                    append("Authorization", "Bearer $it")
                }
            }
        }
    }

    //===========================================
    // WORKOUT HISTORY/SESSION ENDPOINTS (CRUD)
    //===========================================

    /**
     * Fetches all completed workout sessions for the current user.
     *
     * Retrieves workout history with exercises and sets performed. Used to
     * display progress, statistics, and past performance.
     *
     * @return List of history workout DTOs with nested exercise and set data
     */
    suspend fun getHistoryWorkouts(): List<HistoryWorkoutDto> {
        val token = getAuthHeader()
        return client.get("$baseUrl/sessions?include=exercises") {
            token?.let {
                headers {
                    append("Authorization", "Bearer $it")
                }
            }
        }.body()
    }

    /**
     * Uploads a completed workout session to the backend.
     *
     * Saves a finished workout to the cloud for backup and cross-device sync.
     * Includes all exercises performed and sets completed (reps, weight, type).
     *
     * @param session The completed workout session with all performance data
     */
    suspend fun postHistoryWorkout(session: Session) {
        val token = getAuthHeader()
        client.post("$baseUrl/sessions") {
            contentType(ContentType.Application.Json)
            setBody(session)
            token?.let {
                headers {
                    append("Authorization", "Bearer $it")
                }
            }
        }
    }


    suspend fun deleteHistoryWorkout(historyWorkoutId: String) {
        val token = getAuthHeader()
        client.delete("$baseUrl/sessions/$historyWorkoutId") {
            token?.let {
                headers {
                    append("Authorization", "Bearer $it")
                }
            }
        }
    }

    /**
     * Updates an existing history workout on the backend.
     *
     * Syncs local changes to the historyWorkout (name, exercises, sets) with the backend.
     *
     * @param historyWorkout The historyWorkout with updated data
     */
    suspend fun editHistoryWorkout(historyWorkout: HistoryWorkout) {
        val token = getAuthHeader()
        client.put("$baseUrl/session/${historyWorkout.id}") {
            contentType(ContentType.Application.Json)
            setBody(historyWorkout)
            token?.let {
                headers {
                    append("Authorization", "Bearer $it")
                }
            }
        }
    }
}