package com.example.workoutapp.data.database.entities.templates

import androidx.room.Embedded
import androidx.room.Relation


/**
 * Room relationship model for a complete workout template with nested exercises and sets.
 *
 * This is **not** a database entity - it's a data structure used by Room's @Relation
 * annotation to fetch the complete template hierarchy in a single query.
 *
 * **Structure**: TemplateEntity → List<TemplateExerciseWithSets> → List<TemplateSetEntity>
 *
 * Used by DAOs to efficiently load all related data with one database call instead
 * of separate queries for template, exercises, and sets.
 *
 * @property template The root template entity
 * @property exercises List of exercises in this template, each with their sets
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

/**
 * Intermediate relationship model for a template exercise with its planned sets.
 *
 * Part of the nested structure for loading complete templates from Room.
 *
 * @property exercise The exercise entity
 * @property sets All planned sets for this exercise
 */
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