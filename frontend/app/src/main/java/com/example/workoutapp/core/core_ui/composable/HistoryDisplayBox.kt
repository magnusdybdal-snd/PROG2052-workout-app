package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workoutapp.core.core_ui.composable.modifiers.BorderBoxModifier
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.features.history.HistoryViewModel
import com.example.workoutapp.features.home.WorkoutTemplatesViewModel


@Composable
fun HistoryDisplayBox(
    it: HistoryWorkout,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val padding = Modifier.padding(start = 8.dp)
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Row(
        modifier = BorderBoxModifier(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text( // Workout name
                text = it.name,
                fontSize = 20.sp
            )
            Row {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Time Icon",
                )
                Text(
                    text = "%02d:%02d:%02d".format(
                        it.duration.toHours(),
                        it.duration.toMinutes() % 60,
                        it.duration.seconds % 60
                    ),
                    modifier = padding
                )
                Row {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "",
                        modifier = padding
                    )
                    Text( // Total volume
                        text = it.totalVolume.toString(),
                        modifier = padding
                    )
                }
            }
        } // end column 1 "workout text
        Text(
            text = it.date.toString(),
            textAlign = TextAlign.End
        )

        Box {
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
                    text = {
                        Text(text = "Delete session")
                    },
                    onClick = {
                        showDeleteDialog = true
                        expanded = false
                    }
                )
            }
        }
    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("Delete Session?")
            },
            text = { Text("Are you sure you want to delete workout completed on ${it.date}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSession(it)
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
