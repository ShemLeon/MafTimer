package com.leoevg.maftimer.presenter.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.maftimer.R
import com.leoevg.maftimer.navigation.NavigationPaths
import com.leoevg.maftimer.presenter.screens.sections.timer.components.ui.CustomCircle
import com.leoevg.maftimer.presenter.screens.sections.timer.components.ui.DialDivider
import com.leoevg.maftimer.presenter.screens.sections.timer.components.ui.Indicators
import com.leoevg.maftimer.presenter.screens.sections.player.PlayerContainer
import com.leoevg.maftimer.presenter.screens.sections.timer.components.TimerAssembly
import com.leoevg.maftimer.presenter.screens.sections.timer.components.ui.ProgressBar
import com.leoevg.maftimer.presenter.screens.sections.title.TitleApplication
import com.leoevg.maftimer.presenter.util.performStrongVibration

@Composable
fun MainScreen(
    navigate: (NavigationPaths) -> Unit,
    onSpotifyAuthRequest: () -> Unit = {}
) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val state by viewModel.state.collectAsState()

    MainScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        onSpotifyAuthRequest = onSpotifyAuthRequest
    )
}

@Composable
private fun MainScreenContent(
    state: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit,
    onSpotifyAuthRequest: () -> Unit = {}
) {
    // Извлекаем высоту экрана в Dp
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    // Определяем цвета для градиента
    val topGradientColor = Color(0xFF3B3736)    // более светлый оттенок (сверху)
    val bottomGradientColor = Color(0xFF292625) // более темный оттенок (снизу)
    val hapticFeedback = LocalHapticFeedback.current   // вибрация при нажатии
    val context = LocalContext.current

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
                TimerAssembly()
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
            // Три маленьких кружочка сверху
            Indicators()
            PlayerContainer(onSpotifyAuthRequest = onSpotifyAuthRequest)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    MainScreenContent(
        state = MainScreenState(
            progressFraction = 0.3f,
            isRunning = true
        ),
        onEvent = { it -> }
    )
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreviewStart() {
    MainScreenContent(
        state = MainScreenState(progressFraction = 0f, isRunning = false),
        onEvent = { it -> }
    )
}