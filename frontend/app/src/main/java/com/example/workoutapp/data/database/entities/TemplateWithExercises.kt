package com.example.workoutapp.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

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