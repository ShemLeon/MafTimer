package com.leoevg.maftimer.presenter.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.maftimer.R
import com.leoevg.maftimer.navigation.NavigationPaths
import com.leoevg.maftimer.presenter.components.CustomCircle
import com.leoevg.maftimer.presenter.components.DialDivider
import com.leoevg.maftimer.presenter.components.Indicators
import com.leoevg.maftimer.presenter.util.PlayerContainer
import com.leoevg.maftimer.presenter.components.ProgressBar
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
            // Блок с title
            Row(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = "Timer Screen",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 30.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            // Блок с кругом
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .offset(y = -(screenHeightDp * 0.15f))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(1f)
                ) {
                    CustomCircle(
                        color = Color.White,
                        diameterFraction = 1f
                    )
                    ProgressBar(
                        percentage = state.progressFraction, // сектор от 60
                        number = 60,           // текст внутри = seconds
                        color = Color.Green,
                        animDuration = 100, // 10 секунд.
                        strokeWidth = 12.dp
                    )
                    // ───── вместо IconButton { … } ─────
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                            .pointerInput(state.isRunning, state.isPaused, state.isFinished) {
                                detectTapGestures(
                                    onLongPress = {
                                        context.performStrongVibration()       // «сильная» вибрация
                                        if (state.isRunning) {
                                            onEvent(MainScreenEvent.OnBtnTimerPauseClick)
                                        }
                                    },
                                    onTap = {
                                        context.performStrongVibration(
                                            durationMs = 120,
                                            amplitude = 100   // заметно мягче, если устройство поддерживает амплитуду
                                        )
                                        when {
                                            state.isFinished -> {
                                                onEvent(MainScreenEvent.OnBtnTimerResetClick)
                                                onEvent(MainScreenEvent.OnBtnTimerStartClick)
                                            }

                                            state.isRunning -> onEvent(MainScreenEvent.OnBtnTimerResetClick)
                                            state.isPaused -> onEvent(MainScreenEvent.OnBtnTimerResumeClick)
                                            else -> onEvent(MainScreenEvent.OnBtnTimerStartClick)
                                        }
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (state.isRunning) R.drawable.btn_renew
                                else if (state.isPaused) R.drawable.btn_pause
                                else R.drawable.btn_start
                            ),
                            contentDescription = if (state.isRunning) "Renew" else "Start",
                            tint = Color.Black,
                            modifier = Modifier
                                .fillMaxSize(0.45f)
                                .offset(x = if (state.isRunning || state.isPaused) 0.dp else (screenHeightDp * 0.015f))

                        )
                    }
                    // Разделители
                    DialDivider(angleDegrees = 0, color = Color(0x80000000))
                    DialDivider(angleDegrees = 180, color = Color(0xFF3D5AFE))
                    DialDivider(angleDegrees = 60, color = Color(0x80fc520d))

                }
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