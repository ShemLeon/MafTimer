package com.leoevg.maftimer.presenter.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.maftimer.navigation.NavigationPaths
import com.leoevg.maftimer.presenter.screens.sections.timer.components.TypeOfPlayerIndicators
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayer
import com.leoevg.maftimer.presenter.screens.sections.timer.Timer
import com.leoevg.maftimer.presenter.screens.sections.timer.TimerViewModel
import com.leoevg.maftimer.presenter.screens.sections.timer.TimerState
import com.leoevg.maftimer.presenter.screens.sections.timer.TimerEvent
import com.leoevg.maftimer.presenter.screens.sections.title.TitleApplication
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerEvent
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerViewModel

@Composable
fun MainScreen(
    navigate: (NavigationPaths) -> Unit,
    onSpotifyAuthRequest: () -> Unit = {},
    viewModel: MainScreenViewModel = hiltViewModel(),
    timerViewModel: TimerViewModel = hiltViewModel()
) {
    val timerState by timerViewModel.state.collectAsState()
    val musicViewModel: MusicPlayerViewModel = hiltViewModel()
    val musicState by musicViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        if (musicState.isAuthorized){
            musicViewModel.sendEvent(MusicPlayerEvent.OnRefreshPlayback)
        }
    }

    MainScreenContent(
        timerState = timerState,
        onTimerEvent = timerViewModel::onEvent,
        onEvent = viewModel::onEvent,
        onSpotifyAuthRequest = onSpotifyAuthRequest
    )
}

@Composable
private fun MainScreenContent(
    timerState: TimerState,
    onTimerEvent: (TimerEvent) -> Unit,
    onEvent: (MainScreenEvent) -> Unit,
    onSpotifyAuthRequest: () -> Unit = {}
) {
    // Извлекаем высоту экрана в Dp
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    // Определяем цвета для градиента
    val topGradientColor = Color(0xFF3B3736)    // более светлый оттенок (сверху)
    val bottomGradientColor = Color(0xFF292625) // более темный оттенок (снизу)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(topGradientColor, bottomGradientColor)
                )
            )
            .padding(5.dp)
    ) {
        Column() {
            TitleApplication()
            // Блок с кругом
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .offset(y = -(screenHeightDp * 0.15f))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Timer(
                    state = timerState,
                    onEvent = onTimerEvent,
                )
            }
        }
        // Блок для плеера
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TypeOfPlayerIndicators()  // Три маленьких кружочка сверху
            MusicPlayer(onSpotifyAuthRequest = onSpotifyAuthRequest)
        }
    }
}

// TODO: Fix Preview
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
        onEvent = { _: MainScreenEvent -> },
        onSpotifyAuthRequest = {}
    )
}
