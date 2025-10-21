package com.example.workoutapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workoutapp.data.database.entities.TemplateEntity
import kotlinx.coroutines.flow.Flow


/**
 * Data Access Object (DAO) for managing [TemplateEntity] records in the local Rooom database.
 *
 * This interface providees methods for:
 * - Observing all stored templates as a [Flow] for reactive UI updates
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

    @Query("SELECT * FROM templates ORDER BY createdAt DESC")
    fun getAllTemplates(): Flow<List<TemplateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(template: TemplateEntity)

    @Query("SELECT * FROM templates WHERE isSynced = 0")
    suspend fun getUnsyncedTemplates(): List<TemplateEntity>

    @Query("UPDATE templates SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)

    @Query("DELETE FROM templates")
    suspend fun clearAll()

    @Query("SELECT * FROM templates ORDER BY createdAt DESC")
    suspend fun getAllTemplatesSnapshot(): List<TemplateEntity>
}