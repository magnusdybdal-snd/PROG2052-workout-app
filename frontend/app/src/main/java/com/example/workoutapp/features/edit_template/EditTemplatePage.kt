package com.example.workoutapp.features.edit_template

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.workoutapp.R
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.core.core_ui.composable.RoundBackButton
import com.example.workoutapp.core.core_ui.composable.RoundedButton
import com.example.workoutapp.core.core_ui.composable.WorkoutNameTextField
import com.example.workoutapp.core.core_ui.composable.WorkoutTextField
import com.example.workoutapp.domain.models.Set
import com.example.workoutapp.domain.models.TemplateExercise
import com.example.workoutapp.domain.models.WorkoutTemplate
import kotlinx.coroutines.launch

/**
 * Displays Edit Template page
 */
@Composable
fun EditTemplatePage(
    templateId: Int,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: EditTemplateViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val cs = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current

    when {
        state.isLoading -> LoadingStateView()
        state.error != null -> ErrorStateView(state.error)
        else -> {
            val template = state.templates.getOrNull(templateId)
            if (template == null) {
                LoadingStateView()
                return
            }

            val exercises = remember { mutableStateListOf<TemplateExercise>() }
            LaunchedEffect(template) {
                exercises.clear()
                exercises.addAll(template.exercises)
            }

            // state variables for save feedback
            var isSaving by remember { mutableStateOf(false) }
            var showSaveSuccess by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                focusManager.clearFocus()
                            }
                        )
                    }
            ) {
                RoundBackButton(
                    navController = navController,
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    if (showSaveSuccess) {
                        Text(
                            text = "Template saved successfully",
                            color = cs.onBackground,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    RoundedButton(
                        buttonText = if (isSaving) "Saving..." else stringResource(R.string.save_template),
                        onClick = {
                            focusManager.clearFocus() // Clear focus
                            isSaving = true
                            val editedWorkout = WorkoutTemplate(
                                templateId = template.templateId,
                                name = template.name,
                                createdAt = template.createdAt,
                                exercises = template.exercises
                            )
                            viewModel.editTemplate(editedWorkout)
                            showSaveSuccess = true
                            isSaving = false

                            viewModel.viewModelScope.launch {
                                kotlinx.coroutines.delay(1500)
                                navController.popBackStack()
                            }
                        },
                    )

                    WorkoutNameTextField(temp = template)

                    var expanded by remember { mutableStateOf(false) }
                    var searchString by remember { mutableStateOf("") }

                    Box(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Button(onClick = { expanded = !expanded }) {
                            Text(text = stringResource(R.string.add_exercise))
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                                searchString = "" // Reset search when closing
                            },
                            containerColor = cs.tertiary
                        ) {
                            // Search TextField inside the dropdown
                            TextField(
                                value = searchString,
                                onValueChange = { searchString = it },
                                placeholder = {
                                    Text(
                                        text = "Search exercise",
                                        color = cs.onBackground
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )

                            // Filter exercises based on search query
                            val filteredExercises = state.exercises.filter { ex ->
                                template.exercises.none { it.exerciseId == ex.exerciseId } &&
                                        ex.name.contains(searchString, ignoreCase = true)
                            }

                            filteredExercises.forEach { exercise ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = exercise.name,
                                            color = cs.onTertiary
                                        )
                                    },
                                    onClick = {
                                        template.exercises.add(
                                            TemplateExercise(
                                                exerciseId = exercise.exerciseId,
                                                name = exercise.name,
                                                sets = mutableStateListOf(
                                                    Set(
                                                        rep = 0,
                                                        kg = 0.0,
                                                        typeSet = 0,
                                                    )
                                                )
                                            )
                                        )
                                        expanded = !expanded
                                        searchString = ""
                                    }
                                )
                            }

                            if (filteredExercises.isEmpty()) {
                                Text(
                                    text = "No exercises found",
                                    color = cs.onTertiary,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.padding(top = 10.dp),
                    ) {
                        template.exercises.forEachIndexed { exerciseIndex, exSet ->
                            // Exercise name with remove button
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            ) {
                                Text(
                                    exSet.name,
                                    fontSize = 15.sp
                                )
                                IconButton(
                                    onClick = {
                                        template.exercises.removeAt(exerciseIndex)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove exercise"
                                    )
                                }
                            }

                            // Header Row
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.sets),
                                    fontSize = 10.sp,
                                    modifier = Modifier.width(50.dp)
                                )
                                Text(
                                    text = stringResource(R.string.kg),
                                    fontSize = 10.sp,
                                    modifier = Modifier.width(100.dp)
                                )
                                Text(
                                    text = stringResource(R.string.reps),
                                    fontSize = 10.sp,
                                    modifier = Modifier.width(100.dp)
                                )
                                Text(
                                    text = "",
                                    fontSize = 10.sp,
                                    modifier = Modifier.width(50.dp)
                                )
                            }

                            // Loop through each set
                            exSet.sets.forEachIndexed { setIndex, set ->
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp)
                                        .height(75.dp)
                                ) {
                                    // Set Number
                                    Text(
                                        "${setIndex + 1}",
                                        fontSize = 15.sp,
                                        modifier = Modifier.width(50.dp)
                                    )

                                    // KG TextField
                                    WorkoutTextField(
                                        label = stringResource(R.string.kg),
                                        exSet = exSet,
                                        type = "kg",
                                        set = set
                                    )

                                    // Reps TextField
                                    WorkoutTextField(
                                        label = stringResource(R.string.reps),
                                        exSet = exSet,
                                        type = "reps",
                                        set = set
                                    )

                                    // Remove set button
                                    IconButton(
                                        onClick = {
                                            exSet.sets.removeAt(setIndex)
                                        },
                                        modifier = Modifier.width(50.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove set"
                                        )
                                    }
                                }
                            }

                            // Add set button
                            IconButton(
                                onClick = {
                                    template.exercises[exerciseIndex].sets.add(
                                        Set(
                                            rep = 0,
                                            kg = 0.0,
                                            typeSet = 0,
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_set)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}