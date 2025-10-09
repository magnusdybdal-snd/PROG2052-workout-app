package com.example.workoutapp.domain.models

import kotlinx.serialization.Serializable

@Serializable
class Session (
    val sessionId: String,
    val name: String,
    val exercises: List<SessionExercise>,
    val duration: String,
    val date: String,
    val note: String
)
@Serializable
data class SessionExercise(
    val exerciseId: String,
    val sets: List<Set>
)