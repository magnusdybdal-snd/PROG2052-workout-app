package com.example.workoutapp.data.database.entities.history

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Represents a full workout with its nested exercises and sets.
 *
 * Used for fetching the complete structure from the database in one go.
 * This is *not* an entity — it’s a relationship model combining multiple tables.
 */

data class HistoryWorkoutWithExercises(
    @Embedded
    val workout: HistoryWorkoutEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId",
        entity = WorkoutExerciseEntity::class
    )
    val exercises: List<WorkoutExerciseWithSets>
)

data class WorkoutExerciseWithSets(
    @Embedded
    val exercise: WorkoutExerciseEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseEntityId",
        entity = SetEntity::class
    )
    val sets: List<SetEntity>
)