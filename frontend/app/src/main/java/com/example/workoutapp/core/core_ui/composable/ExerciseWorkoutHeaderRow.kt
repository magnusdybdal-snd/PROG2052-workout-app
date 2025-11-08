package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.R

@Composable
fun ExerciseWorkoutHeaderRow() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.sets),
            fontSize = 10.sp,
            modifier = Modifier.width(50.dp)
        )
        Text(
            text = stringResource(R.string.kg),
            fontSize = 10.sp,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = stringResource(R.string.reps),
            fontSize = 10.sp,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = "",
            fontSize = 10.sp,
            modifier = Modifier.width(50.dp)
        )
    }
}