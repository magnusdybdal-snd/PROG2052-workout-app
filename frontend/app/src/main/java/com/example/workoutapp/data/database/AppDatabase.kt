package com.example.workoutapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.workoutapp.data.database.dao.HistoryWorkoutDao
import com.example.workoutapp.data.database.entities.HistoryWorkoutEntity

@Database(
    entities = [HistoryWorkoutEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyWorkoutDao(): HistoryWorkoutDao
}