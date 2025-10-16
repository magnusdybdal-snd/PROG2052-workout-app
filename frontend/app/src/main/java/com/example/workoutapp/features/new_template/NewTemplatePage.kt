package com.example.workoutapp.features.new_template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.core.core_ui.composable.RoundBackButton
import com.example.workoutapp.core.core_ui.composable.RoundedButton
import com.example.workoutapp.core.core_ui.composable.WorkoutTextField
import com.example.workoutapp.core.core_ui.composable.modifiers.BorderBoxModifier
import com.example.workoutapp.core.core_ui.theme.AppTextField
import com.example.workoutapp.domain.models.NewTemplate
import com.example.workoutapp.domain.models.NewTemplateExercise
import com.example.workoutapp.domain.models.Set

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
                            buttonText = "Add template",
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
                                            templateId = "tmp_000",
                                            name = name,
                                            exercises = exercises
                                        )
                                        viewModel.postWorkout(newTemplate)
                                        showDialog = false
                                        navController.popBackStack()
                                    }) {
                                        Text(
                                            text ="Add template",
                                            color = cs.onBackground
                                        )
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showDialog = false }) {
                                        Text(
                                            text = "Cancel",
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
                        label = { Text(text = "Set template name",
                            color = cs.onSecondaryContainer) },
                        shape = RoundedCornerShape(12.dp),
                        colors = AppTextField.fieldColors(),
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                    )

                    var expanded by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Button(onClick = { expanded = !expanded }) {
                            Text("Add exercise")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            containerColor = cs.tertiary
                        ) {
                            state.exercises.forEach { exercise ->
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
                                                sets = mutableListOf(
                                                    Set (
                                                        rep = 0,
                                                        kg = 0,
                                                        typeSet = 0,
                                                    )
                                                )
                                            )
                                        )
                                        expanded = !expanded
                                    }
                                )
                            }
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .padding(top = 10.dp),
                    ) {
                        exercises.forEach { exSet ->
                            Text(
                                exSet.exerciseId,
                                fontSize = 15.sp
                            )
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
                                    modifier = Modifier
                                        .height(h.dp * y)
                                        .fillMaxHeight()
                                ) {
                                    Text(
                                        "SETS",
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                    )
                                    for (i in 1..y) {
                                        Text(
                                            "$i\n",
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
                                        label = "KG",
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
                                    WorkoutTextField(
                                        label = "SETS",
                                        exSet = exSet
                                    )
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
                                    contentDescription = "Add workout"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


