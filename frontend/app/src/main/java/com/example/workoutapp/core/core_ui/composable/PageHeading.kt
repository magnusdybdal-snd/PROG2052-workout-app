package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PageHeading(
    displayText: String,
    cs: ColorScheme = MaterialTheme.colorScheme
) {
    return Text(
            modifier = Modifier
                .padding(
                    top = 40.dp,
                    bottom = 40.dp
                ),
            text = displayText,
            fontSize = 50.sp,
            color = cs.onBackground,
    )
}