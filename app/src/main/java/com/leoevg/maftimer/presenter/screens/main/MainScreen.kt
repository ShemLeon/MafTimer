package com.leoevg.maftimer.presenter.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.leoevg.maftimer.presenter.util.ProgressBar
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.maftimer.navigation.NavigationPaths
import androidx.compose.ui.platform.LocalConfiguration
import com.leoevg.maftimer.presenter.util.CustomCircle
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.leoevg.maftimer.presenter.util.Indicators
import com.leoevg.maftimer.presenter.util.PlayerContainer
import com.leoevg.maftimer.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.input.pointer.pointerInput
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.maftimer.presenter.util.DialDivider

@Composable
fun MainScreen(
    navigate: (NavigationPaths) -> Unit
) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val state by viewModel.state.collectAsState()

    MainScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun MainScreenContent(
    state: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit
) {
    // Извлекаем высоту экрана в Dp
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    // Определяем цвета для градиента
    val topGradientColor = Color(0xFF3B3736) // Более светлый оттенок (сверху)
    val bottomGradientColor = Color(0xFF292625) // Более темный оттенок (снизу)

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
                    IconButton(
                        modifier = Modifier
                            .fillMaxSize(1f)
                            .align(Alignment.Center)
                            .padding(
                                start = if (state.isRunning) 0.dp else 25.dp
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        // Долгое нажатие - ставим на паузу (только если таймер работает)
                                        if (state.isRunning) {
                                            onEvent(MainScreenEvent.OnBtnTimerPauseClick)
                                        }
                                    }
                                )
                            },
                        onClick = {
                            // Обычное нажатие
                            if (state.isFinished) {
                                // Если таймер завершен - запускаем заново
                                onEvent(MainScreenEvent.OnBtnTimerResetClick)
                                onEvent(MainScreenEvent.OnBtnTimerStartClick)
                            } else if (state.isRunning) {
                                // Если таймер работает - сбрасываем
                                onEvent(MainScreenEvent.OnBtnTimerResetClick)
                            } else if (state.isPaused) {
                                // Если на паузе - возобновляем
                                onEvent(MainScreenEvent.OnBtnTimerResumeClick)
                            } else {
                                // Если таймер остановлен - запускаем
                                onEvent(MainScreenEvent.OnBtnTimerStartClick)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (state.isRunning) R.drawable.btn_renew else R.drawable.btn_start
                            ),
                            contentDescription = if (state.isRunning) "Renew" else "Start",
                            tint = Color.Black,
                            modifier = Modifier.fillMaxSize(0.45f),
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
            PlayerContainer()
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

