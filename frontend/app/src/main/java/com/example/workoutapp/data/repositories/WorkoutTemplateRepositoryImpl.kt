package com.example.workoutapp.data.repositories

import android.util.Log
import com.example.workoutapp.data.api.ApiService
import com.example.workoutapp.data.database.dao.templates.TemplateDao
import com.example.workoutapp.data.database.entities.templates.TemplateEntity
import com.example.workoutapp.data.database.entities.templates.TemplateExerciseEntity
import com.example.workoutapp.data.database.entities.templates.TemplateSetEntity
import com.example.workoutapp.domain.models.NewTemplate
import com.example.workoutapp.domain.models.NewTemplateExercise
import com.example.workoutapp.domain.models.Set
import com.example.workoutapp.domain.models.TemplateExercise
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.UUID
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

        return dao.getAllTemplatesWithExercises().map { templates ->
            templates.map { fullTemplate ->
                WorkoutTemplate(
                    templateId = fullTemplate.template.id,
                    name = fullTemplate.template.name,
                    createdAt = fullTemplate.template.createdAt,
                    exercises = fullTemplate.exercises.map { exerciseWithSets ->
                        TemplateExercise(
                            exerciseId = exerciseWithSets.exercise.exerciseId,
                            name = exerciseWithSets.exercise.name,
                            sets = exerciseWithSets.sets.map { setEntity ->
                                Set(
                                    rep = setEntity.rep,
                                    kg = setEntity.kg,
                                    typeSet = setEntity.typeSet
                                )
                            }.toMutableList()
                        )
                    }.toMutableList()
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

            val fullTemplate = dao.getTemplateWithExercises(local.id)

            if (fullTemplate != null) {
                val newTemplate = NewTemplate(
                    templateId = fullTemplate.template.id,
                    name = fullTemplate.template.name,
                    //createdAt = local.createdAt,
                    exercises = fullTemplate.exercises.map { exerciseWithSets ->
                        NewTemplateExercise(
                            exerciseId = exerciseWithSets.exercise.exerciseId,
                            name = exerciseWithSets.exercise.name,
                            sets = exerciseWithSets.sets.map { setEntity ->
                                Set(
                                    rep = setEntity.rep,
                                    kg = setEntity.kg,
                                    typeSet = setEntity.typeSet
                                )
                            }.toMutableList()
                        )
                    }
                )

                try {
                    api.postWorkoutTemplate(newTemplate)
                    dao.markAsSynced(local.id)
                } catch (e: Exception) {
                    Log.w("TemplateRepo", "failed to sync template ${local.id}: ${e.message}")
                }
            }
        }

        // Step 2: Pull latest templates from backend
        val remoteTemplate = try {
            api.getWorkoutTemplates().map { dto ->
                val templateEntity = TemplateEntity(
                    id = dto.templateId,
                    name = dto.name,
                    isSynced = true,
                    createdAt = LocalDateTime.now() // TODO: Backend does not implement this atm
                )

                // Map exercises
                val exerciseEntities = dto.exercises.map { exerciseDto ->
                    TemplateExerciseEntity(
                        id = UUID.randomUUID().toString(),
                        templateId = dto.templateId,
                        exerciseId = exerciseDto.exercise.exerciseId,
                        name = exerciseDto.exercise.name
                    )
                }

                val setEntities = dto.exercises.flatMapIndexed { index, exDto ->
                    val parentExerciseId = exerciseEntities[index].id
                    exDto.sets.map { setDto ->
                        TemplateSetEntity(
                            id = UUID.randomUUID().toString(),
                            exerciseEntityId = parentExerciseId,
                            rep = setDto.rep,
                            kg = setDto.kg,
                            typeSet = setDto.typeSet
                        )
                    }
                }

                Triple(templateEntity, exerciseEntities, setEntities)
            }
        } catch (e: Exception) {
            Log.e("TemplateRepo", "Error fetching templates from API: ${e.message}")
            emptyList()
        }

        // Step 3: Insert locally
        if(remoteTemplate.isNotEmpty()) {
            try {
                dao.insertFullTemplates(remoteTemplate)
            } catch (e: Exception) {
                Log.e("TemplateRepo", "Failed inserting templates: ${e.message}")
            }
        }

        Log.d("TemplateRepo", "Fetched ${remoteTemplate.size} remote templates")
        Log.d("TemplateRepo", "Local DB now has ${dao.getAllTemplatesSnapshot().size} templates")

        // Step 4: Return local data from DB (local first)
        return dao.getAllTemplatesSnapshot()
            .sortedByDescending { it.createdAt }
            .map { entity ->
            WorkoutTemplate(
                templateId = entity.id,
                name = entity.name,
                createdAt = entity.createdAt,
                exercises = mutableListOf() // exercises are handled by Flow observers
            )
        }
    }
    override suspend fun postWorkoutTemplate(newTemplate: NewTemplate) {

        val templateEntity = TemplateEntity(
            id = newTemplate.templateId,
            name = newTemplate.name,
            isSynced = false,
            createdAt = LocalDateTime.now()
        )

        // Create exercise entities
        val exerciseEntities = newTemplate.exercises.map { templateExercise ->
            TemplateExerciseEntity(
                id = UUID.randomUUID().toString(),
                templateId = newTemplate.templateId,
                exerciseId = templateExercise.exerciseId,
                name = templateExercise.name
            )
        }

        // Create set entities
        val setEntities = newTemplate.exercises.flatMapIndexed { idx, templateExercise ->
            val parentExerciseId = exerciseEntities[idx].id

            templateExercise.sets.map { set ->
                TemplateSetEntity(
                    id = UUID.randomUUID().toString(),
                    exerciseEntityId = parentExerciseId,
                    rep = set.rep,
                    kg = set.kg,
                    typeSet = set.typeSet
                )
            }
        }
        dao.insertFullTemplates(listOf(Triple(templateEntity, exerciseEntities, setEntities)))

        try {
            api.postWorkoutTemplate(newTemplate)
            dao.markAsSynced(newTemplate.templateId)
        } catch (e: Exception) {
            Log.w("TemplateRepo", "Template queued for sync: ${newTemplate.templateId}, error: ${e.message}")
        }
    }
    override suspend fun deleteWorkoutTemplate(workoutTemplate: WorkoutTemplate) {
        try {
            api.deleteWorkoutTemplate(workoutTemplate)
            dao.markAsSynced(workoutTemplate.templateId)
        } catch (e: Exception) {
            Log.w("TemplateRepo", "Template queued for sync: ${workoutTemplate.templateId}, error: ${e.message}")
        }
    }
    override suspend fun editWorkoutTemplate(workoutTemplate: WorkoutTemplate) {
        try {
            api.editWorkoutTemplate(workoutTemplate)
            dao.markAsSynced(workoutTemplate.templateId)
        } catch (e: Exception) {
            Log.w("TemplateRepo", "Template queued for sync: ${workoutTemplate.templateId}, error: ${e.message}")
        }
    }
}