package com.example.workoutapp.data.database.entities.templates

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Room entity representing a planned set within a template exercise.
 *
 * Each [TemplateSetEntity] belongs to one [TemplateExerciseEntity] via the
 * `exerciseEntityId` foreign key. Together, they define the planned workout
 * structure (e.g., "3 sets of 10 reps at 50kg").
 *
 * The foreign key is configured with CASCADE delete, meaning when an exercise
 * is removed from a template, all its planned sets are automatically deleted.
 *
 * @property id Auto-generated unique identifier (UUID)
 * @property exerciseEntityId Foreign key linking to parent TemplateExerciseEntity
 * @property rep Planned number of repetitions for this set
 * @property kg Planned weight in kilograms for this set
 * @property typeSet Type of set (0 = normal, 1 = warmup, 2 = drop set, etc.)
 */
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