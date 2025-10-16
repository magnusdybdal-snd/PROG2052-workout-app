package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.core.core_ui.theme.AppTextField
import com.example.workoutapp.domain.models.NewTemplateExercise

/**
 * @param label Information to user, displayed above set.
 * @param exSet data class of an exercise and list of sets.
 */
@Composable
fun WorkoutTextField(
    label: String,
    exSet: NewTemplateExercise,
){
    Text(label, fontSize = 10.sp)
    exSet.sets.forEach { set ->
        TextField(
            value = set.kg.toString(),
            onValueChange = { set.kg = it.toIntOrNull() ?: 0 },
            shape = RoundedCornerShape(12.dp),
            colors = AppTextField.fieldColors(),
            modifier = Modifier
                .width(100.dp)
                .height(50.dp)
        )
    }
}