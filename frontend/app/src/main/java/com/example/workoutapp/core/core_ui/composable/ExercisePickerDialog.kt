package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    AlertDialog(
        onDismissRequest = {
            searchString = ""
            onDismiss()
        },
        title = {
            Text(
                "Select Exercise",
                color = cs.onTertiary
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxSize()) {
                // Search input
                TextField(
                    value = searchString,
                    onValueChange = { searchString = it },
                    placeholder = { Text("Search exercise") },
                    singleLine = true,
                    colors = fieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                // Filtered exercise list
                val filtered = exercises.filter {
                    it.name.contains(searchString, ignoreCase = true)
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
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
                                exercise.name,
                                color = cs.onTertiary
                            )
                        }
                    }
                }

                if (filtered.isEmpty()) {
                    Text("No exercises found", color = cs.onTertiary)
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = {
                searchString = ""
                onDismiss()
            }) {
                Text("Close", color = cs.onTertiary)
            }
        }
    )
}
