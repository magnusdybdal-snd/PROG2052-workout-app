package com.example.workoutapp.data.repositories

import android.util.Log
import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.database.dao.TemplateDao
import com.example.workoutapp.data.database.entities.TemplateEntity
import com.example.workoutapp.domain.models.NewTemplate
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Repository that provides access to templates from both local Room DB and remote API.
 * Room acts as the single source of truth, while the API syncs data in and out.
 */
class WorkoutTemplateRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: TemplateDao
): WorkoutTemplateRepository {


    override fun observeTemplates(): Flow<List<WorkoutTemplate>> {

        return dao.getAllTemplates().map { entities ->
            entities.map { entity ->
                WorkoutTemplate(
                    templateId = entity.id,
                    name = entity.name,
                    exercises = emptyList() // TODO: Add nested structure
                )
            }
        }
    }

    /**
     * Fetch templates from the API and update the local database with fresh API data
     * 1. Push unsynced local templates to the API
     * 2. Pull updated templates from the API and merge them locally
     */
    override suspend fun getWorkoutTemplates(): List<WorkoutTemplate> {
        // Step 1: Post unsynced templates to API
        val unsyncedTemplates = dao.getUnsyncedTemplates()
        unsyncedTemplates.forEach { local ->
            try {
                api.postWorkoutTemplate(
                    newTemplate = NewTemplate(
                        templateId = local.id,
                        name = local.name,
                        exercises = emptyList() // TODO: Nested structure
                    )
                )
                dao.markAsSynced(local.id)
            } catch (e: Exception) {
                Log.w("TemplateRepo", "failed to sync template ${local.id}: ${e.message}")
            }
        }

        // Step 2: Pull latest templates from backend
        val remoteTemplate = try {
            api.getWorkoutTemplates().map { dto ->
                TemplateEntity(
                    id = dto.templateId,
                    name = dto.name,
                    note = "", // TODO: implement notes in templates?
                    isSynced = true,
                    createdAt = LocalDateTime.now() // TODO: Backend does not implement this atm
                )
            }
        } catch (e: Exception) {
            Log.e("TemplateRepo", "Error fetching templates from API: ${e.message}")
            emptyList()
        }

        // Step 3: Insert locally
        if (remoteTemplate.isNotEmpty()) {
            remoteTemplate.forEach { dao.insert(it) }
        }

        // Step 4: Return local data from DB (local first)
        return dao.getAllTemplatesSnapshot().map { entity ->
            WorkoutTemplate(
                templateId = entity.id,
                name = entity.name,
                exercises = emptyList()
            )
        }
    }

    override suspend fun postWorkoutTemplate(newTemplate: NewTemplate) {
        api.postWorkoutTemplate(newTemplate)
    }
}