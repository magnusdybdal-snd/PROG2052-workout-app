package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.workoutapp.core.core_ui.theme.AppTextField.fieldColors
import com.example.workoutapp.domain.models.Exercise

/**
 * Reusable Exercise Picker Dialog with search functionality
 *
 * @param showDialog Controls visibility of the dialog
 * @param exercises List of available exercises to choose from
 * @param onDismiss Callback when dialog is dismissed/closed
 * @param onExerciseSelected Callback when an exercise is selected, receives the selected Exercise
 */
@Composable
fun ExercisePickerDialog(
    showDialog: Boolean,
    exercises: List<Exercise>,
    onDismiss: () -> Unit,
    onExerciseSelected: (Exercise) -> Unit
) {
    if (!showDialog) return

    val cs = MaterialTheme.colorScheme
    var searchString by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = {
            searchString = ""
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = cs.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = "Select Exercise",
                    style = MaterialTheme.typography.headlineMedium,
                    color = cs.onBackground
                )

                // Search input
                TextField(
                    value = searchString,
                    onValueChange = { searchString = it },
                    placeholder = { Text("Search exercise") },
                    singleLine = true,
                    colors = fieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                // Filtered exercise list
                val filtered = exercises.filter {
                    it.name.contains(searchString, ignoreCase = true)
                }

                if (filtered.isEmpty()) {
                    Text(
                        text = "No exercises found",
                        color = cs.onBackground,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(filtered) { exercise ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onExerciseSelected(exercise)
                                        searchString = ""
                                    }
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = exercise.name,
                                    color = cs.onBackground
                                )
                            }
                        }
                    }
                }

                // Close button at bottom
                TextButton(
                    onClick = {
                        searchString = ""
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text(
                        text = "Close",
                        color = cs.onBackground
                    )
                }
            }
        }
    }
}
