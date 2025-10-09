package com.example.workoutapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Duration
import java.time.LocalDate

@Entity(tableName = "history_workouts")
data class HistoryWorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val date: LocalDate,
    val duration: Duration,
    val totalVolume: Double
)