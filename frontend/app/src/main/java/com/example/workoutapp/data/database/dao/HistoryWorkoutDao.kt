package com.example.workoutapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workoutapp.data.database.entities.HistoryWorkoutEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing [HistoryWorkoutEntity] records in the local Room database.
 *
 * This interface provides methods for:
 * - Observing all stored workouts as a [Flow] for reactive UI updates
 * - Inserting or replacing workouts, individually or in bulk
 * - Retrieving workouts that haven’t been synced with the backend API
 * - Marking records as synced once the API confirms successful upload
 * - Clearing all data during a full resync or app reset
 *
 * The DAO serves as the single point of interaction between the repository layer
 * and the underlying SQLite database managed by Room.
 */
@Dao
interface HistoryWorkoutDao {

    // Retrieves all workouts from local ROOM storage, ordered by most recent first.
    // Changes are displayed dynamically when data is changed with Flow
    @Query("SELECT * FROM history_workouts ORDER BY date DESC")
    fun getAllHistoryWorkouts(): Flow<List<HistoryWorkoutEntity>>

    // Inserts or replaces a single workout. Existing entries with the same ID will be replaced
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: HistoryWorkoutEntity)

    // Inserts and replaces multiple workouts in one go. Used for bulk syncing from the API
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workouts: List<HistoryWorkoutEntity>)

    // Gets all the workouts that are flagged as not synced from local ROOM storage
    @Query("SELECT * FROM history_workouts WHERE isSynced = 0")
    suspend fun getUnsyncedWorkouts(): List<HistoryWorkoutEntity>

    // Marks one workout as synced AFTER successfully posting to backend API
    @Query("UPDATE history_workouts SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)

    // Deletes all stored workouts. Used for full sync resets
    @Query("DELETE FROM history_workouts")
    suspend fun clearAll()
}