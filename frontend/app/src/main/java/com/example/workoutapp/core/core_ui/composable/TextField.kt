package com.example.workoutapp.core.core_ui.composable

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableInferredTarget
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.core.core_ui.theme.AppTextField
import com.example.workoutapp.domain.models.NewTemplateExercise
import com.example.workoutapp.domain.models.TemplateExercise

/**
 * Displays text field and corresponding label.
 * @param label Information to user, displayed above set.
 * @param exSet data class of TemplateExercise
 * @see WorkoutTextField Overloaded to handle NewTemplateExercise
 */
@Composable
fun WorkoutTextField(
    label: String,
    exSet: TemplateExercise,
){
    Text(label, fontSize = 10.sp)
    exSet.sets.forEach { set ->
        TextField(
            value = set.kg.toString(),
            onValueChange = { set.kg = it.toIntOrNull() ?: 0 },
            shape = RoundedCornerShape(size = 12.dp),
            colors = AppTextField.fieldColors(),
            modifier = Modifier
                .width(100.dp)
                .height(50.dp)
        )
    }
}

/**
 * Displays text field and corresponding label.
 * @param label Information to user, displayed above set.
 * @param exSet data class of NewTemplateExercise
 * @see WorkoutTextField Overloaded to handle TemplateExercise instance.
 */
@Composable
fun WorkoutTextField(
    label: String,
    exSet: NewTemplateExercise,
) {
    Text(label, fontSize = 10.sp)
    exSet.sets.forEach { set ->
        var text by remember { mutableStateOf(set.kg.toString()) }

        TextField(
            value = text,
            onValueChange = {
                text = it
                set.kg = it.toIntOrNull() ?: 0
            },
            shape = RoundedCornerShape(size = 12.dp),
            colors = AppTextField.fieldColors(),
            modifier = Modifier
                .width(100.dp)
                .height(50.dp)
        )
    }
}