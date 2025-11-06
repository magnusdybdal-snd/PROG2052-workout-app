package com.example.workoutapp.core.core_ui.composable

import android.R
import android.R.attr.singleLine
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableInferredTarget
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workoutapp.core.core_ui.theme.AppTextField
import com.example.workoutapp.core.core_ui.theme.AppTextField.fieldColors
import com.example.workoutapp.domain.models.NewTemplateExercise
import com.example.workoutapp.domain.models.TemplateExercise
import com.example.workoutapp.domain.models.WorkoutTemplate

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
    type: String
){
    Text(label, fontSize = 10.sp)
    exSet.sets.forEach { set ->
        key(set) {
            var text by remember(set) {
                mutableStateOf(
                    when (type.lowercase()) {
                        "kg"   -> set.kg.toString()
                        "reps" -> set.rep.toString()
                        else   -> "0"
                    }
                )
            }

            TextField(
                value = text,
                onValueChange = { newValue: String ->
                    val filtered = newValue.filter { it.isDigit() || it == '.' }

                    if (filtered.isValidDecimal(maxDecimals = 2)) {
                        text = filtered
                        when (type.lowercase()) {
                            "kg" -> set.kg = filtered.toDoubleOrNull() ?: 0.0
                            "reps" -> set.rep = filtered.toIntOrNull() ?: 0
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = AppTextField.fieldColors(),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            )
        }
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
    type: String
){
    Text(label, fontSize = 10.sp)
    exSet.sets.forEach { set ->
        key(set) {
            var text by remember(set) {
                mutableStateOf(
                    when (type.lowercase()) {
                        "kg"   -> set.kg.toString()
                        "reps" -> set.rep.toString()
                        else   -> "0"
                    }
                )
            }

            TextField(
                value = text,
                onValueChange = { newValue: String ->
                    val filtered = newValue.filter { it.isDigit() || it == '.' }

                    if (filtered.isValidDecimal(maxDecimals = 2)) {
                        text = filtered
                        when (type.lowercase()) {
                            "kg" -> set.kg = filtered.toDoubleOrNull() ?: 0.0
                            "reps" -> set.rep = filtered.toIntOrNull() ?: 0
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = AppTextField.fieldColors(),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            )
        }
    }
}

/**
 * Displays text field and corresponding label.
 * @param temp data class of WorkoutTemplate
 * @see WorkoutTextField Overloaded to handle TemplateExercise instance.
 */
@Composable
fun WorkoutNameTextField(
    temp: WorkoutTemplate,
){
    var text by remember {
        mutableStateOf(
            temp.name
        )
    }
    TextField(
        value = text,
        onValueChange = {
            text = it
            temp.name = it
        },
        shape = RoundedCornerShape(12.dp),
        colors = fieldColors(),
        modifier = Modifier.padding(vertical = 10.dp)
    )
}

/**
 * Displays text field and corresponding label.
 */
@Composable
fun TimerTextField(
    time: Int,
    onTimeChange: (Int) -> Unit // Lift the state up
) {
    var text by remember { mutableStateOf(time.toString()) }

    TextField(
        value = text,
        onValueChange = {
            text = it
            // Only update if the input is a valid integer
            it.toIntOrNull()?.let(onTimeChange)
        },
        label = { Text(
            "Timer (minutes)",
            color = MaterialTheme.colorScheme.onBackground
        ) },
        shape = RoundedCornerShape(12.dp),
        colors = fieldColors(),
        modifier = Modifier.padding(vertical = 10.dp)
    )
}

/**
 * Validate that string is a valid decimal
 * @param maxDecimals Maximum decimal precision allowed
 */
private fun String.isValidDecimal(maxDecimals: Int = 2): Boolean{
    if (isEmpty()) return true
    if (count {it == '.'} > 1) return false

    val parts = split(".")
    return if (parts.size == 2) {
        parts[0].all {it.isDigit()} &&
        parts[1].all {it.isDigit()} &&
        parts[2].length <= maxDecimals
    } else{
        all {it.isDigit() || it == '.'}
    }
}
