package com.example.workoutapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(

    @PrimaryKey
    val exerciseId: String,
    val name: String,
    val targetMuscles: List<String>, // JSON array as string
    val bodyParts: List<String>, // JSON array as string
    val equipments: List<String>, // JSON array as string
    val secondaryMuscles: List<String>, // JSON array as string
    val gifUrl: String?,
    val instructions: List<String>, // JSON array as string
    val lastUpdated: Long = System.currentTimeMillis()
)