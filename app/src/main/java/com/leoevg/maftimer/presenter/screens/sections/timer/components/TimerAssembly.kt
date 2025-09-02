package com.leoevg.maftimer.presenter.screens.sections.timer.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.R
import com.leoevg.maftimer.presenter.screens.sections.timer.TimerEvent
import com.leoevg.maftimer.presenter.screens.sections.timer.TimerState
import com.leoevg.maftimer.presenter.screens.sections.timer.components.ui.CustomCircle
import com.leoevg.maftimer.presenter.screens.sections.timer.components.ui.DialDivider
import com.leoevg.maftimer.presenter.screens.sections.timer.components.ui.ProgressBar
import com.leoevg.maftimer.presenter.util.performStrongVibration

@Composable
fun TimerAssembly(
    state: TimerState,
    onEvent: (TimerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
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
                                onEvent(TimerEvent.OnPauseClick)
                            }
                        },
                        onTap = {
                            context.performStrongVibration(
                                durationMs = 120,
                                amplitude = 100   // заметно мягче, если устройство поддерживает амплитуду
                            )
                            when {
                                state.isFinished -> {
                                    onEvent(TimerEvent.OnResetClick)
                                    onEvent(TimerEvent.OnStartClick)
                                }

                                state.isRunning -> onEvent(TimerEvent.OnResetClick)
                                state.isPaused -> onEvent(TimerEvent.OnResumeClick)
                                else -> onEvent(TimerEvent.OnStartClick)
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