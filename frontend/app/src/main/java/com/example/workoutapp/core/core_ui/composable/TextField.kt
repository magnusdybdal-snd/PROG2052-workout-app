package com.example.workoutapp.core.core_ui.composable

import android.R.attr.type
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
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
    type: String,
    set: com.example.workoutapp.domain.models.Set
){
    var text by remember {mutableStateOf("")}
    var isValid by remember {mutableStateOf(true)}

    TextField(
        value = text,
        onValueChange = { newValue: String ->
            text = newValue.filter { it.isDigit() || it == '.' }
        },
        isError = !isValid,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                validateAndSave(text, type, set){valid ->
                isValid = valid
                }
            }
        ),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = AppTextField.fieldColors(),
        modifier = Modifier
            .width(100.dp)
            .height(50.dp)
            .onFocusChanged{ focusState ->
                if (!focusState.isFocused){
                    validateAndSave(text,type,set){valid ->
                        isValid = valid
                    }
                }
            }
    )
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
    type: String,
    set: com.example.workoutapp.domain.models.Set
){
    var text by remember {mutableStateOf("")}
    var isValid by remember {mutableStateOf(true)}

    TextField(
        value = text,
        onValueChange = { newValue: String ->
            val text = newValue.filter { it.isDigit() || it == '.' }
        },
        isError = !isValid,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                validateAndSave(text, type, set){valid ->
                    isValid = valid
                }
            }
        ),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = AppTextField.fieldColors(),
        modifier = Modifier
            .width(100.dp)
            .height(50.dp)
            .onFocusChanged{ focusState ->
                if (!focusState.isFocused){
                    validateAndSave(text,type,set){valid ->
                        isValid = valid
                    }
                }
            }
    )
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

private fun validateAndSave(
    text: String,
    type: String,
    set: com.example.workoutapp.domain.models.Set,
    onValidationResult: (Boolean) -> Unit
) {
    if (text.isEmpty()) {
        when (type.lowercase()) {
            "kg" -> set.kg = 0.0
            "reps" -> set.rep = 0
        }
        onValidationResult(true)
        return
    }

    // Validate the decimal format
    val isValid = text.isValidDecimal(maxDecimals = 2)

    if (isValid) {
        when (type.lowercase()) {
            "kg" -> set.kg = text.toDoubleOrNull() ?: 0.0
            "reps" -> set.rep = text.toDoubleOrNull()?.toInt() ?: 0
        }
    }

    onValidationResult(isValid)
}