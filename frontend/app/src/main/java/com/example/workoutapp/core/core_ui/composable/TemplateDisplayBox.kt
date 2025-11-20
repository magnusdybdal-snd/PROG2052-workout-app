package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.core.core_navigation.Routes
import com.example.workoutapp.core.core_ui.composable.modifiers.BorderBoxModifier
import com.example.workoutapp.domain.models.WorkoutTemplate
import com.example.workoutapp.domain.session_manager.ActiveWorkoutManager
import com.example.workoutapp.features.home.WorkoutTemplatesViewModel

/**
 * Displays  content of template on HomePage
 * @param templateName Name of template
 * @param navController NavigationController
 * @param route route triggered by onClick function.
 */
@Composable
fun TemplateDisplayContent (
    template: WorkoutTemplate,
    navController: NavController,
    index: Int,
    cs: ColorScheme = MaterialTheme.colorScheme,
    viewModel: WorkoutTemplatesViewModel = hiltViewModel(),
) {
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Row(
        modifier = BorderBoxModifier(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = template.name,
            fontSize = 20.sp
        )
        Row {
            StandardButtonCustomRoute(
                buttonText = "Start",
                onClick = {
                    if (!viewModel.activeWorkoutManager.hasActiveWorkout()) {
                        viewModel.activeWorkoutManager.startWorkout(template)
                    }
                    navController.navigate(Routes.WORKTEMP)
                },
                navController = navController,
            )

            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Extra"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Delete template") },
                    onClick = {
                        showDeleteDialog = true
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text(text = "Edit template") },
                    onClick = {
                        navController.navigate("editTemp/$index")
                        expanded = !expanded
                    }
                )
            }
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text ("Delete Template?")
            },
            text = { Text("Are you sure you want to delete ${template.name}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteTemplate(template)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}