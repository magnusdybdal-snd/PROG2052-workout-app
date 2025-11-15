package com.example.workoutapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(

    @PrimaryKey
    val exerciseId: String,
    val name: String,
    val targetMuscles: String, // JSON array as string
    val bodyParts: String, // JSON array as string
    val equipments: String, // JSON array as string
    val secondaryMuscles: String, // JSON array as string
    val gifUrl: String?,
    val instructions: String, // JSON array as string
    val lastUpdated: Long = System.currentTimeMillis()
)