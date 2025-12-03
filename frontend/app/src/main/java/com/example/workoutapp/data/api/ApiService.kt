package com.example.workoutapp.data.api

import android.util.Log
import com.example.workoutapp.data.api.dto.ExampleTemplateDto
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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

    /**
     * Retrieves the JWT token from DataStore for request authentication.
     *
     * @return JWT token string, or null if not logged in
     */
    private suspend fun getAuthHeader(): String? {
        return preferences.token.firstOrNull()
    }

    /**
     * Authenticates the user via Google OAuth.
     *
     * Sends the authorization code from Google Sign-In to the backend, which
     * exchanges it for a JWT token and user profile. The backend creates or
     * retrieves the user account and returns authentication credentials.
     *
     * @param code Authorization code from Google OAuth flow
     * @return AuthResponse containing JWT token, user ID, and display name
     */
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
     * Fetches pre-defined example workout templates.
     *
     * Returns a curated set of example templates that users can explore and use
     * as inspiration for their own workouts. These templates are public and don't
     * require authentication.
     *
     * @return List of example template DTOs with nested exercise data
     */
    suspend fun getExampleTemplates(): List<ExampleTemplateDto> {
        return client.get("$baseUrl/example-templates?include=exercises").body()
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
        return client.get("$baseUrl/sessions") {
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
        // Log the session data being sent
        try {
            val json = Json { prettyPrint = true }
            val jsonString = json.encodeToString(session)
            Log.d("ApiService", "=== POSTING SESSION TO BACKEND ===")
            Log.d("ApiService", "Session ID: ${session.sessionId}")
            Log.d("ApiService", "Session Name: ${session.name}")
            Log.d("ApiService", "Number of Exercises: ${session.exercises.size}")
            Log.d("ApiService", "Full JSON Body:")
            Log.d("ApiService", jsonString)
            Log.d("ApiService", "=====================================")

            // Log each exercise ID for debugging
            session.exercises.forEachIndexed { index, exercise ->
                Log.d("ApiService", "Exercise[$index] ID: ${exercise.exerciseId}, Name: ${exercise.name}, Sets: ${exercise.sets.size}")
            }
        } catch (e: Exception) {
            Log.e("ApiService", "Failed to serialize session for logging: ${e.message}")
        }

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


    /**
     * Deletes a completed workout from history.
     *
     * Permanently removes the workout session from the backend. This is called
     * after local deletion. The workout is removed from workout history and
     * statistics.
     *
     * @param historyWorkoutId UUID of the workout session to delete
     */
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