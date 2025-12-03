package com.example.workoutapp.data.database.entities.templates

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.workoutapp.data.database.Converters
import java.util.UUID

/**
 * Room entity representing a single exercise within a workout template.
 *
 * Each [TemplateExerciseEntity] belongs to one [TemplateEntity] via the
 * `templateId` foreign key, and may have multiple [TemplateSetEntity]
 * records attached to it.
 *
 * The foreign key is configured with CASCADE delete, meaning when a template
 * is deleted, all its exercises are automatically removed from the database.
 *
 * @property id Auto-generated unique identifier (UUID)
 * @property templateId Foreign key linking to the parent TemplateEntity
 * @property exerciseId Reference to the exercise in the exercise library
 * @property name Display name of the exercise (denormalized for efficiency)
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

    val templateId: String,

    val exerciseId: String,
    val name: String,
)