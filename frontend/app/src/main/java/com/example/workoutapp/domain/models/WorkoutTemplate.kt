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
 * Domain model representing a user-created workout template.
 *
 * Templates serve as reusable workout plans that users can instantiate when
 * starting a workout session.
 *
 * @property templateId Unique identifier for this template
 * @property name User-defined name for the workout template
 * @property createdAt Timestamp when the template was created
 * @property exercises Mutable list of exercises included in this template
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
 * Domain model for an exercise within a workout template.
 *
 * Represents the planned configuration of an exercise, including the target number
 * of sets and reps. This differs from [WorkoutExercise] which contains actual
 * performance data, and [Exercise] which is a library reference.
 *
 * @property exerciseId Reference to the exercise in the library
 * @property name Display name of the exercise
 * @property sets Mutable list of planned set configurations
 * @see Exercise
 * @see WorkoutExercise
 */
@Serializable
data class TemplateExercise(
    val exerciseId: String,
    val name: String,
    val sets: MutableList<Set>
)

/**
 * Custom serializer for [LocalDateTime] using ISO-8601 format.
 *
 * Enables Kotlinx Serialization to serialize/deserialize LocalDateTime objects
 * to and from JSON strings in ISO_LOCAL_DATE_TIME format.
 */
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