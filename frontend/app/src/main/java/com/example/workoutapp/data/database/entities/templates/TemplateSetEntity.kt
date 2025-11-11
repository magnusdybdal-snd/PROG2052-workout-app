package com.example.workoutapp.data.database.entities.templates

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "template_sets",
    foreignKeys = [
        ForeignKey(
            entity = TemplateExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseEntityId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class TemplateSetEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    val exerciseEntityId: String,
    val rep: Int,
    val kg: Double,
    val typeSet: Int
)