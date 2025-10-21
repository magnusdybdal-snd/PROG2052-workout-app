package com.example.workoutapp.core.core_ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AppColor.black,
    onPrimary = AppColor.white,
    secondary = AppColor.lightGrey,
    onSecondary = AppColor.darkGrey,
    tertiary = AppColor.lightTeal,
    onTertiary = AppColor.white,
    background = AppColor.black,
    onBackground = AppColor.white,
    surface = AppColor.teal,
    onSurface = AppColor.black,
    onSurfaceVariant = AppColor.white,
    secondaryContainer = AppColor.fadedTeal,
    onSecondaryContainer = AppColor.white,
    outline = AppColor.lightGrey
)

private val LightColorScheme = lightColorScheme(
    primary = AppColor.white,
    onPrimary = AppColor.black,
    secondary = AppColor.darkGrey,
    onSecondary = AppColor.lightGrey,
    tertiary = AppColor.teal,
    onTertiary = AppColor.white,
    background = AppColor.white,
    onBackground = AppColor.black,
    surface = AppColor.teal,
    onSurface = AppColor.black,
    onSurfaceVariant = AppColor.white,
    secondaryContainer = AppColor.fadedTeal,
    onSecondaryContainer = AppColor.black,
    outline = AppColor.darkGrey
)

@Composable
fun WorkoutAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Colors used in styling of NavigationBarItem.
 * @return NavigationBarIdemDefault with colors from appropriate theme.
 */
object AppNavBar {
    @Composable
    fun itemColors(): NavigationBarItemColors {
        val cs = MaterialTheme.colorScheme      // "cs" is short for "ColorScheme"
        return NavigationBarItemDefaults.colors(
            selectedIconColor = cs.onSecondaryContainer,
            selectedTextColor = cs.onSurfaceVariant,
            indicatorColor = cs.background,
            unselectedIconColor = cs.onSurfaceVariant,
            unselectedTextColor = cs.onSurfaceVariant,
            disabledIconColor = cs.onSurface.copy(alpha = 0.38f),
            disabledTextColor = cs.onSurface.copy(alpha = 0.38f)
        )
    }
}
object AppTextField {
    @Composable
    fun fieldColors(): TextFieldColors {
        val cs = MaterialTheme.colorScheme
        return TextFieldDefaults.colors(
            focusedTextColor = cs.onSecondaryContainer,
            unfocusedTextColor = cs.onSecondaryContainer.copy(alpha = 0.9f),
            disabledTextColor = cs.onSurface.copy(alpha = 0.38f),

            focusedContainerColor = cs.secondaryContainer,
            unfocusedContainerColor = cs.secondaryContainer.copy(alpha = 0.3f),
            disabledContainerColor = cs.surfaceVariant.copy(alpha = 0.3f),

            cursorColor = cs.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    }
}
object AppOutlinedTextField {
    @Composable
    fun outlinedFieldColors(): TextFieldColors {
        val cs = MaterialTheme.colorScheme
        return OutlinedTextFieldDefaults.colors(
            focusedBorderColor = cs.primary,
            unfocusedBorderColor = cs.outline,
            cursorColor = cs.primary,
            focusedLabelColor = cs.primary,
            unfocusedLabelColor = cs.onSurfaceVariant,
            focusedTextColor = cs.onSurface,
            unfocusedTextColor = cs.onSurface,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )
    }
}

object AppCheckBox {
    @Composable
    fun checkBoxColor(): CheckboxColors {
        val cs = MaterialTheme.colorScheme
        return CheckboxDefaults.colors(
            checkedColor = cs.secondaryContainer,
            uncheckedColor = cs.onBackground.copy(alpha = 0.3f),
            checkmarkColor = cs.onBackground,
            disabledCheckedColor = cs.surfaceVariant.copy(alpha = 0.3f),
            disabledUncheckedColor = cs.surfaceVariant.copy(alpha = 0.3f)
        )

    }
}

object AppTextButton {
    @Composable
    fun textButtonColor(): ButtonColors {
        val cs = MaterialTheme.colorScheme
        return ButtonColors(
            containerColor = cs.tertiary,
            contentColor =cs.onTertiary,
            disabledContainerColor = cs.secondaryContainer,
            disabledContentColor = cs.onSecondaryContainer
        )

    }
}