package com.example.workoutapp.features.new_template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.R
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.core.core_ui.composable.RoundBackButton
import com.example.workoutapp.core.core_ui.composable.RoundedButton
import com.example.workoutapp.core.core_ui.composable.WorkoutTextField
import com.example.workoutapp.core.core_ui.composable.modifiers.BorderBoxModifier
import com.example.workoutapp.core.core_ui.composable.modifiers.TextFieldModifier
import com.example.workoutapp.core.core_ui.theme.AppTextField
import com.example.workoutapp.domain.models.NewTemplate
import com.example.workoutapp.domain.models.NewTemplateExercise
import com.example.workoutapp.domain.models.Set
import java.util.UUID

/**
 * Displays Workout page
 */
@Composable
fun NewTemplatePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: NewTempViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val cs = MaterialTheme.colorScheme

    when {
        state.isLoading -> LoadingStateView()
        state.error != null -> ErrorStateView(state.error)
        else -> {
            var name by remember { mutableStateOf("") }
            val exercises = remember { mutableStateListOf<NewTemplateExercise>() }
            val exerciseNames = remember { mutableStateListOf<String>() }

            Column(
                modifier = modifier.verticalScroll(
                    state = rememberScrollState()
                ),
            ) {
                RoundBackButton(
                    navController = navController,
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Row(
                        modifier = BorderBoxModifier(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(viewModel.getCurrentTimeString(), fontSize = 20.sp)
                        var showDialog by remember { mutableStateOf(false) }

                        RoundedButton(
                            buttonText = stringResource(R.string.add_template),
                            onClick = {showDialog = true},
                        )

                        if (showDialog &&
                            name != "" &&
                            exercises.isNotEmpty()) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                title = {
                                    Text(
                                        text = "Complete template?",
                                        color = cs.onBackground
                                    )
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        val newTemplate = NewTemplate(
                                            templateId = UUID.randomUUID().toString(),
                                            name = name,
                                            exercises = exercises
                                        )
                                        viewModel.postWorkout(newTemplate)
                                        showDialog = false
                                        navController.popBackStack()
                                    }) {
                                        Text(
                                            text =stringResource(R.string.add_template),
                                            color = cs.onBackground
                                        )
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showDialog = false }) {
                                        Text(
                                            text = stringResource(R.string.cancel),
                                            color = cs.onBackground
                                        )
                                    }
                                }
                            )
                        }
                    }

                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(text = stringResource(R.string.set_template_name),
                            color = cs.onSecondaryContainer) },
                        shape = RoundedCornerShape(12.dp),
                        colors = AppTextField.fieldColors(),
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                    )

                    var expanded by remember { mutableStateOf(false) }
                    var searchString by remember { mutableStateOf("") }

                    Box(
                        modifier = Modifier
                            .padding(16.dp)
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
                                placeholder = { Text(
                                    text = "Search exercise",
                                    color = cs.onBackground
                                ) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )

                            // Filter exercises based on search query
                            val filteredExercises = state.exercises.filter {
                                it.name.contains(searchString, ignoreCase = true)
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
                                        exercises.add(
                                            NewTemplateExercise(
                                                exerciseId = exercise.exerciseId,
                                                name = exercise.name,
                                                sets = mutableStateListOf(
                                                    Set (
                                                        rep = 0,
                                                        kg = 0,
                                                        typeSet = 0,
                                                    )
                                                )
                                            )
                                        )
                                        exerciseNames.add(exercise.name)
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
                        modifier = Modifier
                            .padding(top = 10.dp),
                    ) {
                        exercises.forEachIndexed { index, exSet ->
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)

                            ) {
                                Text(
                                    exerciseNames[index],
                                    fontSize = 15.sp
                                )
                                IconButton (
                                    onClick = {
                                        exercises.removeAt(index)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove exercise"
                                    )
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)

                            ) {
                                val y = exSet.sets.size
                                val h = 75
                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = TextFieldModifier(height = h.dp * y)
                                ) {
                                    Text(
                                        text = stringResource(R.string.sets),
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                    )
                                    for (i in 1..y) {
                                        Text(
                                            text = "$i\n",
                                            fontSize = 15.sp,
                                        )
                                    }
                                }
                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .height(h.dp * y)
                                        .fillMaxHeight()
                                ) {
                                    WorkoutTextField(
                                        label = stringResource(R.string.sets),
                                        exSet = exSet
                                    )
                                }
                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = TextFieldModifier(height = h.dp * y)
                                ) {
                                    WorkoutTextField(
                                        label = stringResource(R.string.reps),
                                        exSet = exSet
                                    )
                                }
                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .height(h.dp * y)
                                        .fillMaxHeight()
                                ) {
                                    Text("", fontSize = 10.sp)
                                    exSet.sets.forEachIndexed {index, set ->
                                        IconButton (
                                            onClick = {
                                                exSet.sets.removeAt(index)
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove set"
                                            )
                                        }
                                    }
                                }
                            }
                            IconButton (
                                onClick = {
                                    exSet.sets.add(
                                        Set(
                                            rep = 0,
                                            kg = 0,
                                            typeSet = 0,
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.add_workout)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}