package com.example.workoutapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.workoutapp.data.database.entities.history.HistoryWorkoutEntity
import com.example.workoutapp.data.database.entities.history.HistoryWorkoutWithExercises
import com.example.workoutapp.data.database.entities.history.SetEntity
import com.example.workoutapp.data.database.entities.history.WorkoutExerciseEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing [com.example.workoutapp.data.database.entities.history.HistoryWorkoutEntity] records in the local Room database.
 *
 * This interface provides methods for:
 * - Observing all stored workouts as a [kotlinx.coroutines.flow.Flow] for reactive UI updates
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

    @Query("SELECT * FROM history_workouts WHERE isDeleted = 0 ORDER BY date DESC")
    fun getAllHistoryWorkouts(): Flow<List<HistoryWorkoutEntity>>

    @Query("SELECT * FROM history_workouts WHERE isDeleted = 1 AND isSynced = 1")
    suspend fun getDeletedAndSyncedHistoryWorkouts(): List<HistoryWorkoutEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(workout: HistoryWorkoutEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(workouts: List<HistoryWorkoutEntity>)

    @Query("SELECT * FROM history_workouts WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<HistoryWorkoutEntity?>

    @Query("SELECT * FROM history_workouts WHERE isSynced = 0 AND isDeleted = 0")
    suspend fun getUnsyncedWorkouts(): List<HistoryWorkoutEntity>

    @Query("UPDATE history_workouts SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)

    @Query("UPDATE history_workouts SET isSynced = 0 WHERE id = :id")
    suspend fun markAsUnsynced(id: String)

    @Transaction
    @Query("DELETE FROM history_workouts")
    suspend fun clearAll()

    @Transaction
    @Query("DELETE FROM history_workouts WHERE id = :id")
    suspend fun deleteWorkoutById(id: String)

    @Query("UPDATE history_workouts SET isDeleted = 1 WHERE id = :id")
    suspend fun markAsDeleted(id: String)

    @Query("DELETE FROM workout_exercises WHERE workoutId = :workoutId")
    suspend fun deleteExercisesByWorkoutId(workoutId: String)

    // Get all workouts from ROOM once (not reactive)
    @Query("SELECT * FROM history_workouts WHERE isDeleted = 0 ORDER BY date DESC")
    suspend fun getAllHistoryWorkoutsSnapshot(): List<HistoryWorkoutEntity>

    @Transaction
    suspend fun updateHistoryWorkoutExercises(
        workoutId: String,
        historyWorkout: HistoryWorkoutEntity,
        exercises: List<WorkoutExerciseEntity>,
        sets: List<SetEntity>
    ) {
        deleteExercisesByWorkoutId(workoutId)
        insert(historyWorkout)
        insertExercises(exercises)
        insertSets(sets)
    }

    //--------------------------
    //  Nested relationships
    //--------------------------

    @Transaction
    @Query("SELECT * FROM history_workouts WHERE isDeleted = 0 ORDER BY date DESC")
    fun getAllHistoryWorkoutsWithExercises(): Flow<List<HistoryWorkoutWithExercises>>

    @Transaction
    @Query("SELECT * FROM history_workouts WHERE id = :id LIMIT 1")
    suspend fun getWorkoutHistoryWithExercises(id: String): HistoryWorkoutWithExercises?

    //--------------------------
    //  Nested inserts
    //--------------------------

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertExercise(exercise: WorkoutExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertExercises(exercises: List<WorkoutExerciseEntity>)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertSet(set: SetEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertSets(sets: List<SetEntity>)

    //--------------------------
    //  Combined transactional insert
    //--------------------------

    /**
     * Inserts a full workout (parent + exercises + sets) in one atomic transaction.
     * Prevents foreign key violations by ensuring the parent is inserted first.
     */
    @Transaction
    suspend fun insertFullWorkout(
        workouts: List<Triple<HistoryWorkoutEntity, List<WorkoutExerciseEntity>, List<SetEntity>>>
    ) {
        workouts.forEach { (workout, exercises, sets) ->
            insert(workout)
            insertExercises(exercises)
            insertSets(sets)
        }
    }
}