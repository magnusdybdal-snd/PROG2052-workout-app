package com.example.workoutapp.features.exercises

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.R
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.domain.models.Exercise

@Composable
fun ExercisesPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ExercisesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val cs = MaterialTheme.colorScheme

    when {
        state.isLoading -> LoadingStateView()
        state.error != null -> ErrorStateView(state.error)
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 700.dp)
                    .background(cs.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text( // Title of page
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .padding(bottom = 20.dp),
                    text = "Exercises",
                    fontSize = 50.sp,
                    color = cs.onBackground,
                )
                Column( // All exercises
                    modifier = modifier
                        .fillMaxSize()
                        .widthIn(max = 550.dp)
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    state.exercises.forEach { exercise: Exercise ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(80.dp, 80.dp)
                                .padding(vertical = 6.dp)
                                .border(width = 2.dp, color = cs.onBackground),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image( // Picture of exercise
                                painter = painterResource(id = R.drawable.exampleworkoutimage),
                                contentDescription = "Exercise image",
                                contentScale = ContentScale.Inside,
                                modifier = Modifier
                                    .heightIn(80.dp, 80.dp)
                                    .padding(8.dp)
                            )
                            Text( // Exercise name
                                modifier = Modifier.padding(6.dp),
                                text = exercise.name,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
