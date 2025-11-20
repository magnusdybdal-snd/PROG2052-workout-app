package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.workoutapp.R
import com.example.workoutapp.core.core_ui.composable.modifiers.BorderBoxModifier
import com.example.workoutapp.domain.models.HistoryWorkout
import com.example.workoutapp.features.history.HistoryViewModel
import java.time.Duration

// Constants for consistent styling
private object HistoryDisplayConstants {
    val WORKOUT_NAME_TEXT_SIZE = 20.sp
    val DETAIL_TEXT_SIZE = 12.sp
    val ICON_SIZE = 16.dp
    val DETAIL_START_PADDING = 8.dp
    val VOLUME_SPACING = 12.dp
    val DATE_START_PADDING = 16.dp
    val ICON_OFFSET = 8.dp
}

// Extension function for duration formatting
private fun Duration.toFormattedString(): String =
    "%02d:%02d:%02d".format(toHours(), toMinutes() % 60, seconds % 60)


@Composable
fun HistoryDisplayBox(
    workout: HistoryWorkout,
    cs: ColorScheme = MaterialTheme.colorScheme,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val detailPadding = Modifier.padding(start = HistoryDisplayConstants.DETAIL_START_PADDING)
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dialogButtonColors = ButtonDefaults.textButtonColors(
        contentColor = cs.onBackground
    )

    Row(
        modifier = BorderBoxModifier(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = workout.name,
                fontSize = HistoryDisplayConstants.WORKOUT_NAME_TEXT_SIZE
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_timer),
                    contentDescription = "Duration",
                    modifier = Modifier.size(HistoryDisplayConstants.ICON_SIZE)
                )
                Text(
                    text = workout.duration.toFormattedString(),
                    fontSize = HistoryDisplayConstants.DETAIL_TEXT_SIZE,
                    modifier = detailPadding
                )

                Spacer(Modifier.width(HistoryDisplayConstants.VOLUME_SPACING))

                Icon(
                    painter = painterResource(R.drawable.ic_exercise),
                    contentDescription = "Volume",
                    modifier = Modifier.size(HistoryDisplayConstants.ICON_SIZE)
                )
                Text(
                    text = workout.totalVolume.toString(),
                    fontSize = HistoryDisplayConstants.DETAIL_TEXT_SIZE,
                    modifier = detailPadding
                )
            }
        }
        Text(
            text = workout.date.toString(),
            textAlign = TextAlign.End,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .padding(start = HistoryDisplayConstants.DATE_START_PADDING)
        )

        Box {
            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.offset(x = HistoryDisplayConstants.ICON_OFFSET)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options"
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
            text = { Text("Are you sure you want to delete workout completed on ${workout.date}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSession(workout)
                        showDeleteDialog = false
                    },
                    colors = dialogButtonColors
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    },
                    colors = dialogButtonColors
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
