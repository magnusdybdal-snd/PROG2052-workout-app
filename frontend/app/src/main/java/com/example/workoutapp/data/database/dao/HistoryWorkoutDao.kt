package com.example.workoutapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workoutapp.domain.models.HistoryWorkout
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryWorkoutDao {

    @Query("SELECT * FROM history_workouts ORDER BY date DESC")
    fun getAllHistoryWorkouts(): Flow<List<HistoryWorkout>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: HistoryWorkout)

    @Query("DELETE FROM history_workouts")
    suspend fun clearAll()
}