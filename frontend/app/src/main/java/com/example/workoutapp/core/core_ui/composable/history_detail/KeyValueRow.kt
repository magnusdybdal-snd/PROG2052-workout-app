package com.example.workoutapp.core.core_ui.composable.history_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Displays a simple labeled value pair used for workout metadata.
 *
 * Commonly used in the history detail screen to show attributes such as
 * date, duration, total volume, or notes.
 *
 * @param label The descriptive text shown above the value (e.g. "Date", "Duration").
 * @param value The corresponding value to display below the label.
 * @param cs The [ColorScheme] providing theme colors for consistent styling.
 */
@Composable
fun KeyValueRow(
    label: String,
    value: String,
    cs: ColorScheme
) {
    Column(Modifier.padding(vertical = 2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = cs.onBackground.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = cs.onBackground
        )
    }
}