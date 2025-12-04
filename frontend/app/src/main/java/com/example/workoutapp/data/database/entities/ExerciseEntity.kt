package com.example.workoutapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for exercises in the local exercise library.
 *
 * Stores ~1500 pre-loaded exercises from assets/exercises.json. This table
 * is read-only after initialization - exercises are not modified or synced
 * with the backend.
 *
 * List fields (targetMuscles, bodyParts, etc.) are automatically converted
 * to/from JSON by Room's TypeConverters (see Converters.kt).
 *
 * @property exerciseId Unique identifier for the exercise (primary key)
 * @property name Display name of the exercise
 * @property targetMuscles Primary muscle groups worked (stored as JSON array)
 * @property bodyParts Body regions involved (stored as JSON array)
 * @property equipments Required equipment (stored as JSON array)
 * @property secondaryMuscles Additional muscles engaged (stored as JSON array)
 * @property gifUrl URL to animated GIF demonstration (nullable)
 * @property instructions Step-by-step instructions (stored as JSON array)
 * @property lastUpdated Timestamp of last update (currently unused, for future sync)
 */
@Entity(tableName = "exercises")
data class ExerciseEntity(

    @PrimaryKey
    val exerciseId: String,
    val name: String,
    val targetMuscles: List<String>,
    val bodyParts: List<String>,
    val equipments: List<String>,
    val secondaryMuscles: List<String>,
    val gifUrl: String?,
    val instructions: List<String>,
    val lastUpdated: Long = System.currentTimeMillis()
)