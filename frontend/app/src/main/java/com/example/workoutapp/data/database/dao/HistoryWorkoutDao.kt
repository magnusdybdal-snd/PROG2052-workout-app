package com.example.workoutapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.workoutapp.data.database.entities.HistoryWorkoutEntity
import com.example.workoutapp.data.database.entities.HistoryWorkoutWithExercises
import com.example.workoutapp.data.database.entities.SetEntity
import com.example.workoutapp.data.database.entities.WorkoutExerciseEntity
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

    //--------------------------
    //  Basic operations
    //--------------------------

    @Query("SELECT * FROM history_workouts ORDER BY date DESC")
    fun getAllHistoryWorkouts(): Flow<List<HistoryWorkoutEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: HistoryWorkoutEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workouts: List<HistoryWorkoutEntity>)

    @Query("SELECT * FROM history_workouts WHERE isSynced = 0")
    suspend fun getUnsyncedWorkouts(): List<HistoryWorkoutEntity>

    @Query("UPDATE history_workouts SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)

    @Query("DELETE FROM history_workouts")
    suspend fun clearAll()

    // Get all workouts from ROOM once (not reactive)
    @Query("SELECT * FROM history_workouts ORDER BY date DESC")
    suspend fun getAllHistoryWorkoutsSnapshot(): List<HistoryWorkoutEntity>

    //--------------------------
    //  Nested relationships
    //--------------------------

    @Transaction
    @Query("SELECT * FROM history_workouts ORDER BY date DESC")
    fun getAllHistoryWorkoutsWithExercises(): Flow<List<HistoryWorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM history_workouts WHERE id = :id LIMIT 1")
    suspend fun getWorkoutHistoryWithExercises(id: String): HistoryWorkoutWithExercises?

    //--------------------------
    //  Nested inserts
    //--------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: WorkoutExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<WorkoutExerciseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: SetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSets(sets: List<SetEntity>)


}