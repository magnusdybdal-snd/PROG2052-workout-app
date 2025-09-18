package com.example.workoutapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.workoutapp.pages.ExercisesPage
import com.example.workoutapp.pages.HistoryPage
import com.example.workoutapp.pages.WorkTemp
import com.example.workoutapp.pages.WorkoutPage

/**
 * Hosts the bottombar for application
 */
@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    // List of items used in NavigationBarItem
    val navItemList = listOf(
        NavItem("History", Icons.Default.DateRange),
        NavItem("Workouts", Icons.Default.PlayArrow),
        NavItem("Exercises", Icons.Default.Person),
        NavItem("Test", Icons.Default.Build)
    )

    // Initial index for NavigationBarItem
    var selectedIndex by remember {
        mutableIntStateOf(1) // default index is Workout-page.
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                NavigationBarItem(
                    selected = selectedIndex == index,
                    onClick = {
                        selectedIndex = index
                    },
                    icon = {
                        Icon(
                            imageVector = navItem.icon,
                            contentDescription = navItem.label
                        )
                    },
                    label = {
                        Text(navItem.label)
                    }
                )
            }
        }
    }
    ) { innerPadding ->
        ContentScreen(modifier = modifier.padding(innerPadding), selectedIndex)
    }
}

/**
 * Displays page(screen) based on 'bottom navigation bar'
 * @see HistoryPage
 * @see WorkoutPage
 * @see ExercisesPage
 * @param selectedIndex index changes based on what NavigationBarItem is selected.
 */
@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex : Int){
    when (selectedIndex){
        0-> HistoryPage()
        3-> WorkoutPage(modifier)
        2-> ExercisesPage()
        1-> WorkTemp("Test", modifier)
    }
}