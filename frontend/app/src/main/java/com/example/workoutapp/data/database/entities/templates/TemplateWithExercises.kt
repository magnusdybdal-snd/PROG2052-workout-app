package com.example.workoutapp.data.database.entities.templates

import androidx.room.Embedded
import androidx.room.Relation


/**
 * Represents a full template with its nested exercises and sets.
 *
 * Used for fetching the complete structure from the database in one go.
 * This is not an entity - It's a relationship model combining multiple tables.
 */
data class TemplateWithExercises(
    @Embedded
    val template: TemplateEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "templateId",
        entity = TemplateExerciseEntity::class
    )
    val exercises: List<TemplateExerciseWithSets>
)

data class TemplateExerciseWithSets(
    @Embedded
    val exercise: TemplateExerciseEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "exerciseEntityId",
        entity = TemplateSetEntity::class
    )
    val sets: List<TemplateSetEntity>
)