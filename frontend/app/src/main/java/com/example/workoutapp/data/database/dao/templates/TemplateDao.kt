package com.example.workoutapp.data.database.dao.templates

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.workoutapp.data.database.entities.templates.TemplateEntity
import com.example.workoutapp.data.database.entities.templates.TemplateExerciseEntity
import com.example.workoutapp.data.database.entities.templates.TemplateSetEntity
import com.example.workoutapp.data.database.entities.templates.TemplateWithExercises
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing [TemplateEntity] records in the local Rooom database.
 *
 * This interface providees methods for:
 * - Observing all stored templates as a [kotlinx.coroutines.flow.Flow] for reactive UI updates
 * - Inserting or replacing templates
 * - Retrieving templates that haven't been synced with the backend API
 * - Marking records as synced once the API confirms successful upload
 * - Clearing all data during a full resync or app reset
 *
 * The DAO serves as the single point of interaction between the repository layer and
 * the underlying SQLite database managed by Room.
 */
@Dao
interface TemplateDao {

    //--------------------------
    //  Basic operations
    //--------------------------

    // Get all templates that are not marked for deletion
    @Query("SELECT * FROM templates WHERE isDeleted = 0 ORDER BY createdAt DESC")
    fun getAllTemplates(): Flow<List<TemplateEntity>>

    // Gets all templates that are synced to API and marked for delete.
    @Query("SELECT * FROM templates WHERE isDeleted = 1 AND isSynced = 1")
    fun getDeletedAndSyncedTemplates(): List<TemplateEntity>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(template: TemplateEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(templates: List<TemplateEntity>)

    @Query("SELECT * FROM templates WHERE isSynced = 0")
    suspend fun getUnsyncedTemplates(): List<TemplateEntity>

    @Query("UPDATE templates SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)

    // Get all templates from Room once (not reactive)
    @Query("SELECT * FROM templates ORDER BY createdAt DESC")
    suspend fun getAllTemplatesSnapshot(): List<TemplateEntity>

    @Delete
    suspend fun delete(template: TemplateEntity)

    @Query("UPDATE templates SET isDeleted = 1 WHERE id = :id")
    suspend fun markAsDeleted(id: String)

    @Transaction
    @Query("DELETE FROM templates")
    suspend fun clearAll()

    //--------------------------
    //  Nested relationships
    //--------------------------

    @Transaction
    @Query("SELECT * FROM templates ORDER BY createdAt DESC")
    fun getAllTemplatesWithExercises(): Flow<List<TemplateWithExercises>>

    @Transaction
    @Query("SELECT * FROM templates WHERE id = :id LIMIT 1")
    suspend fun getTemplateWithExercises(id: String): TemplateWithExercises?

    //--------------------------
    //  Nested inserts
    //--------------------------

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: TemplateExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<TemplateExerciseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: TemplateSetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSets(sets: List<TemplateSetEntity>)

    //--------------------------
    //  Combined transactional insert
    //--------------------------

    /**
     * Inserts a full template (parent + exercises + sets) in one atomic transaction.
     * Prevents foreign key violations by ensuring the parent is inserted first
     */
    @Transaction
    suspend fun insertFullTemplates(
        templates: List<Triple<TemplateEntity, List<TemplateExerciseEntity>, List<TemplateSetEntity>>>
    ) {
        templates.forEach { (template, exercises, sets) ->
            insert(template)
            insertExercises(exercises)
            insertSets(sets)
        }
    }
}