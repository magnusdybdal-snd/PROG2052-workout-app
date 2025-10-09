package com.example.workoutapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [/*Entities in database*/], version = 1)
abstract class AppDatabase : RoomDatabase() {
    // abstract fun nameDao(): NameDao
}