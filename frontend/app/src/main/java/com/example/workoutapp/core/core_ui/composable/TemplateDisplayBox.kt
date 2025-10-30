package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import com.example.workoutapp.core.core_navigation.Routes
import com.example.workoutapp.core.core_ui.composable.modifiers.BorderBoxModifier
import com.example.workoutapp.domain.models.NewTemplateExercise
import com.example.workoutapp.domain.models.Set
import com.example.workoutapp.domain.models.WorkoutTemplate
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
    viewModel: WorkoutTemplatesViewModel = hiltViewModel()
) {
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
            StandardButton(
                buttonText = "Start",
                navController = navController,
                route = "worktemp/$index"
            )
            var expanded by remember { mutableStateOf(false) }

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
                },
                //containerColor = cs.tertiary
            ) {
                // Search TextField inside the dropdown

                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Delete template",
                            //color = cs.onTertiary
                        )
                    },
                    onClick = {
                        viewModel.deleteTemplate(template)
                        expanded = !expanded
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Edit template",
                            //color = cs.onTertiary
                        )
                    },
                    onClick = {
                        navController.navigate("editTemp/$index")
                        expanded = !expanded
                    }
                )
            }
        }
    }
}