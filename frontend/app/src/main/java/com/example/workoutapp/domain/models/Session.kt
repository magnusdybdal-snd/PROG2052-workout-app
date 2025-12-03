package com.example.workoutapp.domain.models

import com.example.workoutapp.data.serializers.DurationSerializer
import com.example.workoutapp.data.serializers.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.LocalDate

/**
 * Domain model representing a workout session.
 *
 * @property sessionId Unique identifier for this session
 * @property name Name of the workout session
 * @property exercises List of exercises performed in this session
 * @property duration Total duration of the workout session
 * @property date Date when the session took place
 * @property note Optional user note about the session
 */
@Serializable
data class Session (
    val sessionId: String,
    val name: String,
    val exercises: List<SessionExercise>,
    @Serializable(with = DurationSerializer::class)
    val duration: Duration,
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val note: String = ""
)

/**
 * Domain model for an exercise within a session.
 *
 * @property exerciseId Reference to the exercise in the library
 * @property name Display name of the exercise
 * @property sets List of sets performed for this exercise
 */
@Serializable
data class SessionExercise(
    val exerciseId: String,
    val name: String,
    val sets: List<Set>
)