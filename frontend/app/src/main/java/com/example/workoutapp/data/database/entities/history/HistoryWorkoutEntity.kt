package com.example.workoutapp.data.database.entities.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration
import java.time.LocalDate
import java.util.UUID

/**
 * Represents a completed workout stored locally in the Room database.
 *
 * This entity mirrors the data structure used by the backend API but is optimized
 * for local persistence and offline-first functionality. Workouts can be created
 * locally and later synchronized with the backend when online.
 *
 * @property id Globally unique identifier for the workout (UUID string).
 *               Generated locally to ensure offline-safe creation and sync.
 * @property name The workout's name (e.g., "Push Day" or "Legs & Core").
 * @property date The date when the workout was completed.
 * @property duration Total time spent on the workout.
 * @property note Optional user note or comment attached to the workout.
 * @property isSynced Indicates whether this record has been successfully synced
 *                    with the backend API. New or edited local workouts start as `false`.
 * @property lastModified Timestamp (in millis) of the last local modification.
 *                        Used for conflict resolution during sync.
 */
@Entity(tableName = "history_workouts")
data class HistoryWorkoutEntity(

    /** Globally unique ID generated locally (UUID-based, virtually collision-free). */
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    /** User-facing name of the workout. */
    val name: String,

    /** Date when the workout was performed. */
    val date: LocalDate,

    /** Total duration of the workout session. */
    val duration: Duration,

    /** Optional personal note for the workout. */
    val note: String = "",

    /** Whether this workout is synced with the remote backend. */
    val isSynced: Boolean = false,

    /** Wether this workout is marked for deletion */
    val isDeleted: Boolean = false,

    /** Last modification timestamp (used to detect and resolve sync conflicts). */
    val lastModified: Long = System.currentTimeMillis()
)
