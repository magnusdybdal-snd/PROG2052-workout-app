package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.core.core_ui.composable.modifiers.BorderBoxModifier
import com.example.workoutapp.domain.models.HistoryWorkout


@Composable
fun HistoryDisplayBox(it : HistoryWorkout){
    val padding = Modifier.padding(start = 8.dp)
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
                            it.duration.toSeconds() % 60
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
        }
    }
