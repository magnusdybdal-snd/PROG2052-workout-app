package com.example.workoutapp.core.core_ui.composable

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

/**
 * Standard text for Template heading in HomePage
 * @param displayText
 * @param cs color scheme
 * @return Text element with standard size and color.
 */
@Composable
fun TemplateCategoryHeading(
    displayText: String,
    cs: ColorScheme = MaterialTheme.colorScheme
){
    return Text(
        text = displayText,
        fontSize = 30.sp,
        color = cs.onBackground
    )
}