package com.example.workoutapp.data.database.entities.templates

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

/**
 * Represents a workout template stored locally in the Room database.
 *
 * This entity mirrors the data structure used by the backend API but is optimized
 * for local persistence and offline-first functionality. Templates can be created
 * locally and later synchronized with the backend when online.
 *
 * @property id Globally unique identifier for the template (UUID string).
 *               Generated locally to ensure offline-safe creation and sync.
 * @property name The templates name (e.g., "Push Day" or "Legs & Core").
 * @property note Optional user note or comment attached to the template.
 * @property isSynced Indicates whether this record has been successfully synced
 *                    with the backend API. New or edited local templates start as `false`.
 * @property createdAt Timestamp for when the template was created
 */
@Entity(tableName = "templates")
data class TemplateEntity(

    /** Globally unique ID generated locally (UUID-based, virtually collision-free). */
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    /** User-facing name of the template. */
    val name: String,

    /** Whether this workout is synced with the remote backend. */
    val isSynced: Boolean = false,

    /** Mark the template for deletion. Used to make sure API and Room are synced, also when offline */
    val isDeleted : Boolean = false,
    val isExample : Boolean = false,

    /** Created timestamp. */
    val createdAt: LocalDateTime
)
