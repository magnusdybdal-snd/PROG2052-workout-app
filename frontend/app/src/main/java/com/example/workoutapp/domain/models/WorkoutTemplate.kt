package com.example.workoutapp.domain.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Data class for a WorkoutTemplate.
 * Contains a list of TemplateExercise, not to be mixed with WorkoutExercise or Exercise
 *
 * @see TemplateExercise
 */
@Serializable
data class WorkoutTemplate(
    val templateId: String,
    var name: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val exercises: MutableList<TemplateExercise>
)

/**
 * Data class for an exercise belonging to a template.
 * This is different from an exercise belonging to a workout (WorkoutExercise)
 * and different from an exercise in the library (Exercise)
 *
 * @see Exercise
 */
@Serializable
data class TemplateExercise(
    val exerciseId: String,
    val name: String,
    val sets: MutableList<Set>
)

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime =
        LocalDateTime.parse(decoder.decodeString(), formatter)
}