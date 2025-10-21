package com.example.workoutapp.data.database.entities.history

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Represents one set performed within a workout exercise.
 *
 * Each [SetEntity] belongs to one [WorkoutExerciseEntity].
 * Together, they describe all repetitions, weight, and type of the set.
 *
 * @property id Auto-generated local ID (not synced to API).
 * @property exerciseEntityId Foreign key linking to the parent [WorkoutExerciseEntity].
 * @property rep Number of repetitions.
 * @property kg Weight used in kilograms.
 * @property typeSet Indicates type of set (e.g., warmup, working, failure).
 */
@Entity(
    tableName = "sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseEntityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class SetEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    val exerciseEntityId: String,
    val rep: Int,
    val kg: Int,
    val typeSet: Int
)