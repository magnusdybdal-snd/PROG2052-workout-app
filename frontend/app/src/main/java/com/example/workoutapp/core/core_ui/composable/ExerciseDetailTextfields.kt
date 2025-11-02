package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseDetailTextField(
    label: String,
    values: List<String>
) {
    val cs = MaterialTheme.colorScheme
    Row {
        Text(
            text = "$label: ",
            fontWeight = FontWeight.Bold,
            color = cs.onBackground

        )
        Text(
            text = values.joinToString(", "),
            color = cs.onBackground
        )
    }
    Spacer(Modifier.height(6.dp))
}