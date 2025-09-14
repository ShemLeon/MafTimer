package com.leoevg.maftimer.presenter.screens.main


import com.leoevg.maftimer.presenter.util.Logx
import androidx.compose.foundation.Image
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalWindowInfo

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.maftimer.navigation.NavigationPaths
import com.leoevg.maftimer.presenter.screens.sections.timer.TimerViewModel
import com.leoevg.maftimer.presenter.screens.sections.timer.TimerState
import com.leoevg.maftimer.presenter.screens.sections.timer.TimerEvent
import com.leoevg.maftimer.presenter.screens.sections.title.TitleApplication
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerEvent
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerViewModel
import com.leoevg.maftimer.presenter.screens.sections.timer.components.TimerAssembly

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayer

import com.leoevg.maftimer.presenter.screens.sections.player.components.TypeOfPlayerIndicators
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.CustomOverlay
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.MusicAssembly
import com.leoevg.maftimer.presenter.screens.sections.player.local.LocalPlayer

private const val TAG = "MainScreen"

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
    Logx.info(TAG, "Music state updated: isAuthorized=${musicState.isAuthorized}")
    Logx.debug(TAG, "Music state: $musicState")

    // Логика наблюдения жизненного цикла
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            Logx.info(TAG, "Lifecycle event: $event")
            if (event == Lifecycle.Event.ON_RESUME) {
                Logx.action(TAG, "App resumed → checking authorization")
                musicViewModel.sendEvent(MusicPlayerEvent.OnCheckAuthorization)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        // Remove observer when composable leaves composition
        kotlinx.coroutines.DisposableHandle {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
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
        onEvent = viewModel::onEvent,
        onSpotifyAuthRequest = onSpotifyAuthRequest,
        musicPlayerState = musicState,
        onMusicPlayerEvent = { event ->
            Logx.debug(TAG, "Music event: $event")
            musicViewModel.sendEvent(event)
        }
    )
}

@Composable
private fun MainScreenContent(
    timerState: TimerState,
    onTimerEvent: (TimerEvent) -> Unit,
    onEvent: (MainScreenEvent) -> Unit,
    onSpotifyAuthRequest: () -> Unit = {},
    musicPlayerState: MusicPlayerState? = null,
    onMusicPlayerEvent: ((MusicPlayerEvent) -> Unit)? = null
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
        }
        // Блок для плеера
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MusicPlayer(
                onSpotifyAuthRequest = onSpotifyAuthRequest,
                state = musicPlayerState,
                onEvent = onMusicPlayerEvent
            )

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
        onEvent = { _: MainScreenEvent -> },
        onSpotifyAuthRequest = {},
        musicPlayerState = MusicPlayerState(
            isAuthorized = false,
            artist = "Preview Artist",
            title = "Preview Song",
            isPlaying = false,
            progressMs = 60000L,
            durationMs = 180000L
        ),
        onMusicPlayerEvent = {}
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
        onEvent = { _: MainScreenEvent -> },
        onSpotifyAuthRequest = {},
        musicPlayerState = MusicPlayerState(
            isAuthorized = true,
            artist = "Preview Artist",
            title = "Preview Song",
            isPlaying = false,
            progressMs = 60000L,
            durationMs = 180000L
        ),
        onMusicPlayerEvent = {}
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
        onEvent = { _: MainScreenEvent -> },
        onSpotifyAuthRequest = {},
        musicPlayerState = MusicPlayerState(
            isAuthorized = false,  // Or your condition for local overlay
            selectedPage = 0
        ),
        onMusicPlayerEvent = {}
    )
}
