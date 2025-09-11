package com.example.workoutapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    // List of items used in bottombar
    val navItemList = listOf(
        NavItem("History", Icons.Default.DateRange),
        NavItem("Workouts", Icons.Default.PlayArrow),
        NavItem("Exercises", Icons.Default.Person)
    )

    // Index for what bottombar-item is selected.
    var selectedIndex by remember {
        mutableIntStateOf(0)
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
        ContentScreen(modifier = modifier.padding(innerPadding))
    }
}


@Composable
fun ContentScreen(modifier: Modifier = Modifier){

}