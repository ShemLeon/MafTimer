package com.leoevg.maftimer.presentation.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.presentation.screens.sections.player.MusicPlayerState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.maftimer.presentation.navigation.NavigationPaths
import com.leoevg.maftimer.presentation.screens.sections.timer.TimerViewModel
import com.leoevg.maftimer.presentation.screens.sections.timer.TimerState
import com.leoevg.maftimer.presentation.screens.sections.timer.TimerEvent
import com.leoevg.maftimer.presentation.screens.sections.title.TitleApplication
import com.leoevg.maftimer.presentation.screens.sections.timer.components.TimerAssembly
import com.leoevg.maftimer.presentation.screens.sections.player.MusicPlayer

private const val TAG = "MainScreen"

@Composable
fun MainScreen(
    navigate: (NavigationPaths) -> Unit,
    viewModel: MainScreenViewModel = hiltViewModel(),
    timerViewModel: TimerViewModel = hiltViewModel()
) {
    val timerState by timerViewModel.state.collectAsState()

    MainScreenContent(
        timerState = timerState,
        onTimerEvent = timerViewModel::onEvent,
        navigate = navigate
    )
}

@Composable
private fun MainScreenContent(
    timerState: TimerState,
    onTimerEvent: (TimerEvent) -> Unit,
    navigate: (NavigationPaths) -> Unit
) {
    val topGradientColor = Color(0xFF111827)    // gray-900 (from)
    val middleGradientColor = Color(0xFF1F2937) // gray-800 (via)
    val bottomGradientColor = Color(0xFF111827) // gray-900 (to)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        topGradientColor,
                        middleGradientColor,
                        bottomGradientColor
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),  // Fixed fillMaxSize to prevent shifting
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleApplication()
            TimerAssembly(state = timerState, onEvent = onTimerEvent)
            Spacer(Modifier.weight(1f))
            MusicPlayer(navigate = navigate)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    MainScreenContent(
        timerState = TimerState(
            progressFraction = 0.3f,
            isRunning = false,
            isPaused = false,
            isFinished = false,
            remainingSeconds = 42
        ),
        onTimerEvent = {},
        navigate = {}
    )
}


@Preview(showBackground = true)
@Composable
private fun MainScreenWithoutOverlayPreview() {
    MainScreenContent(
        timerState = TimerState(
            progressFraction = 0.3f,
            isRunning = false,
            isPaused = false,
            isFinished = false,
            remainingSeconds = 42
        ),
        onTimerEvent = {},
        navigate = {}
    )


}

// 3. Preview: Local overlay (selectedPage = 0, adjust condition if needed, e.g. isLocalLoaded = false)
@Preview(showBackground = true)
@Composable
private fun MainScreenLocalOverlayPreview() {
    MainScreenContent(
        timerState = TimerState(
            progressFraction = 0.3f,
            isRunning = false,
            isPaused = false,
            isFinished = false,
            remainingSeconds = 42
        ),
        onTimerEvent = {},
        navigate = {}
    )
}


