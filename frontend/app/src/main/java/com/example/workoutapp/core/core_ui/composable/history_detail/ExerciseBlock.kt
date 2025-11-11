package com.example.workoutapp.core.core_ui.composable.history_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.R
import com.example.workoutapp.domain.models.Set
import com.example.workoutapp.domain.models.WorkoutExercise

/**
 * Displays a single exercise block inside a workout history detail page.
 *
 * Shows the exercise name, a header row (Sets / Reps / Kg / Volume),
 * and all recorded sets with their corresponding values.
 * Also displays the total exercise volume aligned to the right.
 *
 * @param index Index number of the exercise in the workout (1-based).
 * @param exercise The [WorkoutExercise] containing name, sets, and computed volume.
 * @param cs The active [ColorScheme] used for consistent theming.
 */
@Composable
fun ExerciseBlock(index: Int, exercise: WorkoutExercise, cs: ColorScheme) {
    // Exercise title
    Text(
        text = "$index. ${exercise.name}",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = cs.onBackground
    )

    Spacer(Modifier.height(6.dp))

    // Header row for sets
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(stringResource(R.string.sets),   color = cs.onBackground.copy(alpha = 0.8f))
        Text(stringResource(R.string.kg),     color = cs.onBackground.copy(alpha = 0.8f))
        Text(stringResource(R.string.reps),   color = cs.onBackground.copy(alpha = 0.8f))
        Text(stringResource(R.string.volume), color = cs.onBackground.copy(alpha = 0.8f))
    }

    Spacer(Modifier.height(4.dp))

    // Each set row
    exercise.sets.forEachIndexed { i, set ->
        SetRow(
            setNumber = i + 1,
            set = set,
            cs = cs
        )
    }

    // Per-exercise volume
    Spacer(Modifier.height(6.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Exercise volume: ${exercise.volume}",
            style = MaterialTheme.typography.bodyMedium,
            color = cs.onBackground.copy(alpha = 0.9f),
        )
    }
}

/**
 * Renders a single set row displaying its number, reps, weight, and calculated volume.
 *
 * @param setNumber Sequential number of the set within the exercise.
 * @param set The [Set] object containing rep, kg, and typeSet data.
 * @param cs The [ColorScheme] for color styling.
 */
@Composable
private fun SetRow(
    setNumber: Int,
    set: Set,
    cs: ColorScheme
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("$setNumber", color = cs.onBackground)
        Text("${set.kg}", color = cs.onBackground)
        Text("${set.rep}", color = cs.onBackground)
        Text("${set.volume}", color = cs.onBackground)
    }
}