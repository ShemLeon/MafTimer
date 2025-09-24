package com.leoevg.maftimer.presentation.screens.main

import com.leoevg.maftimer.presentation.util.Logx
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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

//// Принудительная проверка каждые 3 секунды
//    LaunchedEffect(Unit) {
//        while (true) {
//            kotlinx.coroutines.delay(3000)
//            Logx.debug(TAG, "Periodic check → checking authorization")
//            musicViewModel.sendEvent(MusicPlayerEvent.OnCheckAuthorization)
//        }
//    }

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
    val topGradientColor = Color(0xFF3B3736)    // более светлый оттенок (сверху)
    val bottomGradientColor = Color(0xFF292625) // более темный оттенок (снизу)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        topGradientColor,
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
