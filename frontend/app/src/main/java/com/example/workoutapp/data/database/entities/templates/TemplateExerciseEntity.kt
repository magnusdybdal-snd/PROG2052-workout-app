package com.example.workoutapp.data.database.entities.templates

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.workoutapp.data.database.Converters
import java.util.UUID

/**
 * Represents a single exercise within a template.
 *
 * Each [TemplateExerciseEntity] belongs to one [TemplateEntity],
 * and may have multiple [TemplateSetEntity] attatched to it.
 *
 *
 */

@Entity(
    tableName = "template_exercise",
    foreignKeys = [
        ForeignKey(
            entity = TemplateEntity::class,
            parentColumns = ["id"],
            childColumns = ["templateId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class TemplateExerciseEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    val templateId: String, // FK to parent template

    // Exercise metadata
    val exerciseId: String,
    val name: String,
)