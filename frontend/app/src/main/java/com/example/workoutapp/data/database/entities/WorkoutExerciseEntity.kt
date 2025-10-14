package com.example.workoutapp.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents a single exercise performed within a completed workout.
 *
 * Each [WorkoutExerciseEntity] belongs to one [HistoryWorkoutEntity],
 * and may have multiple [SetEntity] records attached to it.
 *
 * @property id Auto-generated local ID (not synced to API).
 * @property workoutId Foreign key linking to the parent [HistoryWorkoutEntity].
 * @property exerciseId Reference to the exercise definition (from Exercise table or API).
 * @property name Display name of the exercise.
 */
@Entity(
    tableName = "workout_exercises",
    foreignKeys = [
        ForeignKey(
            entity = HistoryWorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class WorkoutExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val workoutId: String,  // FK to parent workout
    val exerciseId: String, // Matches exercise from API
    val name: String

)