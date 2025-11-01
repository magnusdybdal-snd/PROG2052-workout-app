package com.example.workoutapp.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class NewTemplate (
    val templateId: String,
    var name: String,
    val exercises: List<NewTemplateExercise>
)
@Serializable
data class NewTemplateExercise(
    val exerciseId: String,
    val name: String,
    val sets: MutableList<Set>
)