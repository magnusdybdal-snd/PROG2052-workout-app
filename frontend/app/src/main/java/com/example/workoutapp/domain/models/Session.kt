package com.example.workoutapp.domain.models

import com.example.workoutapp.data.serializers.DurationSerializer
import com.example.workoutapp.data.serializers.LocalDateSerializer
import kotlinx.serialization.Serializable

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
@Serializable
data class SessionExercise(
    val exerciseId: String,
    val name: String,
    val sets: List<Set>
)