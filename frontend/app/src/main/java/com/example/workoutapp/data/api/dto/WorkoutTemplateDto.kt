package com.example.workoutapp.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Data transfer object for a user-created workout template.
 *
 * Represents a workout plan with exercises and planned sets.
 * Received from the backend and converted to domain WorkoutTemplate model.
 *
 * @property templateId Unique identifier for this template
 * @property name User-defined name for the template (e.g., "Push Day", "Full Body")
 * @property exercises List of exercises included in this template with planned sets
 */
@Serializable
data class WorkoutTemplateDto(
    val templateId: String,
    var name: String,
    val exercises: List<TemplateExerciseDto>
)

/**
 * Data transfer object for an exercise within a workout template.
 *
 * Contains full exercise details (from the library) plus the planned sets
 * for this template. The nested structure allows complete exercise metadata
 * to be available without additional lookups.
 *
 * @property exercise Complete exercise data including name, muscles, instructions
 * @property sets List of planned sets with target reps and weight
 */
@Serializable
data class TemplateExerciseDto(
    val exercise: ExerciseDto,
    val sets: List<SetDto>
)

/**
 * Data transfer object for pre-defined example templates.
 *
 * Example templates are curated workout plans provided by the app.
 * Uses a flat structure (exercise ID + name) to reduce payload size
 * since full exercise details are already in the local database.
 *
 * @property templateId Unique identifier for this example template
 * @property name Name of the example template (e.g., "Beginner Full Body")
 * @property exercises List of exercises with planned sets
 */
@Serializable
data class ExampleTemplateDto(
    val templateId: String,
    val name: String,
    val exercises: List<ExampleTemplateExerciseDto>
)

/**
 * Data transfer object for an exercise within an example template.
 *
 * Uses a flat structure with only exercise ID and name rather than
 * full exercise details, since the client can look up the full exercise
 * from the local database using the exerciseId.
 *
 * @property exerciseId Unique identifier matching the exercise library
 * @property name Display name of the exercise
 * @property sets List of planned sets for this exercise
 */
@Serializable
data class ExampleTemplateExerciseDto(
    val exerciseId: String,
    val name: String,
    val sets: List<SetDto>
)