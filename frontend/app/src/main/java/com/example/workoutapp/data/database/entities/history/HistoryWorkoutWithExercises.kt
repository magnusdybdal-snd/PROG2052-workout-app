package com.example.workoutapp.data.database.entities.history

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Room relationship model for a complete workout history entry with nested exercises and sets.
 *
 * This is **not** a database entity - it's a data structure used by Room's @Relation
 * annotation to fetch the complete workout hierarchy in a single query.
 *
 * **Structure**: HistoryWorkoutEntity → List<WorkoutExerciseWithSets> → List<SetEntity>
 *
 * Used by DAOs to efficiently load all related data with one database call instead
 * of separate queries for workout, exercises, and sets.
 *
 * @property workout The root workout entity
 * @property exercises List of exercises performed in this workout, each with completed sets
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

/**
 * Intermediate relationship model for a workout exercise with its completed sets.
 *
 * Part of the nested structure for loading complete workout history from Room.
 *
 * @property exercise The exercise entity
 * @property sets All sets completed for this exercise
 */
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