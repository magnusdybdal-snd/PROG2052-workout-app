package com.example.workoutapp.features.history_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.workoutapp.core.core_ui.composable.ErrorStateView
import com.example.workoutapp.core.core_ui.composable.LoadingStateView
import com.example.workoutapp.core.core_ui.composable.PageHeading
import com.example.workoutapp.core.core_ui.composable.RoundBackButton
import com.example.workoutapp.core.core_ui.composable.history_detail.ExerciseBlock
import com.example.workoutapp.core.core_ui.composable.history_detail.KeyValueRow
import com.example.workoutapp.core.core_ui.composable.modifiers.PageColumnModifier
import com.example.workoutapp.domain.models.HistoryWorkout
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HistoryDetailPage(
    sessionId: String, // kept for parity with your call site; VM uses SavedStateHandle
    navController: NavController,
    viewModel: HistoryDetailViewModel = hiltViewModel(),
    cs: ColorScheme = MaterialTheme.colorScheme
) {
    val ui by viewModel.uiState.collectAsState()

    when {
        ui.isLoading -> LoadingStateView()
        ui.error != null -> ErrorStateView(ui.error)
        else -> {
            val session = ui.session ?: return

            Surface(
                color = cs.background,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = PageColumnModifier()
                ) {

                    // --- Header ---
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        RoundBackButton(
                            onClick = { navController.popBackStack() },
                            cs = cs,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                        PageHeading(displayText = "Session")
                    } // header end

                    // --- Scrollable Body ---
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Title
                        Text(
                            text = session.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = PageTitleFontSize,
                            lineHeight = PageTitleLineHeight,
                            color = cs.onBackground,
                        )

                        Spacer(Modifier.height(12.dp))

                        // Metadata row(s)
                        KeyValueRow("Date", session.formatDate(), cs)
                        KeyValueRow("Duration", session.formatDuration(), cs)
                        KeyValueRow("Total volume", "${session.totalVolume}", cs)

                        if (session.note.isNotBlank()) {
                            Spacer(Modifier.height(8.dp))
                            KeyValueRow("Note", session.note, cs)
                        }

                        Spacer(Modifier.height(16.dp))

                        // Exercises + Sets
                        session.exercises.forEachIndexed { idx, ex ->
                            ExerciseBlock(
                                index = idx + 1,
                                exercise = ex,
                                cs = cs,
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                        Spacer(Modifier.height(24.dp))
                    } // column end
                }
            }
        }
    }
}

// ---------- UI bits  ----------

private val PageTitleFontSize = 36.sp
private val PageTitleLineHeight = 30.sp

// ---------- Formatting helpers ----------
private fun HistoryWorkout.formatDate(): String {
    // Example: Sat 1 Nov 2025 (adjust to your locale/style)
    val fmt = DateTimeFormatter.ofPattern("EEE d MMM yyyy", Locale.getDefault())
    return date.format(fmt)
}

private fun HistoryWorkout.formatDuration(): String {
    val totalSeconds = duration.seconds
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    return buildString {
        if (h > 0) append("${h}h ")
        if (m > 0 || h > 0) append("${m}m ")
    }.trim()
}