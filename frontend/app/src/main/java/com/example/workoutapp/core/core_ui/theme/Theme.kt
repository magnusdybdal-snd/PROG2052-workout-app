package com.example.workoutapp.core.core_ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlin.contracts.Returns

object Color{

}

private val DarkColorScheme = darkColorScheme(
    primary = AppColor.black,
    secondary = AppColor.lightGrey,
    onPrimary = AppColor.white,
    tertiary = AppColor.darkTeal,
    onTertiary = AppColor.white,
    background = AppColor.black,
    onBackground = AppColor.white,
    surface = AppColor.teal,
    onSurface = AppColor.black,
    secondaryContainer = AppColor.black,
    onSecondaryContainer = AppColor.white,
    onSurfaceVariant = AppColor.white,
)

private val LightColorScheme = lightColorScheme(
    primary = AppColor.white,
    secondary = AppColor.darkGrey,
    onPrimary = AppColor.black,
    tertiary = AppColor.teal,
    onTertiary = AppColor.white,
    background = AppColor.white,
    onBackground = AppColor.black,
    surface = AppColor.teal,
    onSurface = AppColor.black,
    secondaryContainer = AppColor.white,
    onSecondaryContainer = AppColor.black,
    onSurfaceVariant = AppColor.white,
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
            indicatorColor = cs.secondaryContainer,
            unselectedIconColor = cs.onSurfaceVariant,
            unselectedTextColor = cs.onSurfaceVariant,
            disabledIconColor = cs.onSurface.copy(alpha = 0.38f),
            disabledTextColor = cs.onSurface.copy(alpha = 0.38f)
        )
    }
}