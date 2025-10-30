package com.example.workoutapp.features.edit_template

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.R
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.core.core_ui.composable.RoundBackButton
import com.example.workoutapp.core.core_ui.composable.RoundedButton
import com.example.workoutapp.core.core_ui.composable.WorkoutNameTextField
import com.example.workoutapp.core.core_ui.composable.WorkoutTextField
import com.example.workoutapp.core.core_ui.composable.modifiers.BorderBoxModifier
import com.example.workoutapp.core.core_ui.composable.modifiers.TextFieldModifier
import com.example.workoutapp.core.core_ui.theme.AppCheckBox
import com.example.workoutapp.core.core_ui.theme.AppOutlinedTextField.outlinedFieldColors
import com.example.workoutapp.core.core_ui.theme.AppTextButton.textButtonColor
import com.example.workoutapp.core.core_ui.theme.AppTextField.fieldColors
import com.example.workoutapp.domain.models.Session
import com.example.workoutapp.domain.models.SessionExercise
import com.example.workoutapp.domain.models.Set
import com.example.workoutapp.domain.models.TemplateExercise
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.repositories.WorkoutTemplateRepository
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

/**viewmodel
 * Displays Workout page
 */
@Composable
fun EditTemplatePage(
    templateId: Int,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ActWorkViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val cs = MaterialTheme.colorScheme

    when {
        state.isLoading -> LoadingStateView()
        state.error != null -> ErrorStateView(state.error)
        else -> {

            val template = state.templates.getOrNull(templateId)
            if (template == null) {
                LoadingStateView()
                return
            }

            Column(
                modifier = Modifier.verticalScroll(
                    state= rememberScrollState()
                ),
            ) {
                RoundBackButton(
                    navController = navController,
                    //modifier = Modifier.padding(innerPadding)
                )
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    RoundedButton(
                        buttonText = stringResource(R.string.save_template),
                        onClick = {
                            val editedWorkout = WorkoutTemplate(
                                templateId = template.templateId,
                                name = template.name,
                                createdAt = template.createdAt,
                                exercises = template.exercises
                            )
                            viewModel.editTemplate(editedWorkout)
                            navController.popBackStack()
                        },
                    )

                    WorkoutNameTextField(temp = template)


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
                                                        kg = 0,
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


                    Text(
                        state.templates[templateId].name,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .padding(top = 10.dp),
                    ) {
                        template.exercises.forEachIndexed { exerciseIndex, exSet ->
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)

                            ) {
                                Text(
                                    exSet.name,
                                    fontSize = 15.sp
                                )
                                IconButton (
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
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            ) {
                                val y = exSet.sets.size //icon
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
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .height(50.dp)
                                        ) {
                                            Text(
                                                "$i",
                                                fontSize = 15.sp
                                            )
                                        }
                                    }
                                }
                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = TextFieldModifier(height = h.dp * y)
                                ) {
                                    WorkoutTextField(
                                        label = stringResource(R.string.kg),
                                        exSet = exSet,
                                        type = "kg"
                                    )
                                }
                                Column(
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = TextFieldModifier(height = h.dp * y)
                                ) {
                                    WorkoutTextField(
                                        label = stringResource(R.string.reps),
                                        exSet = exSet,
                                        type = "reps"
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
                                    template.exercises[exerciseIndex].sets.add(
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