package com.leoevg.maftimer.presentation.screens.sections.timer.components

import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.presentation.screens.sections.timer.TimerEvent
import com.leoevg.maftimer.presentation.screens.sections.timer.TimerState
import com.leoevg.maftimer.presentation.util.performStrongVibration

@Composable
fun TimerAssembly(
    state: TimerState,
    onEvent: (TimerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val windowInfo = LocalWindowInfo.current
    val screenHeightDp = windowInfo.containerSize.height.dp
    Box(
        modifier = Modifier
            .padding(top = 100.dp)
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
            color = Color(0x8078F900),
            animDuration = 100, // 10 секунд.
            strokeWidth = 12.dp
        )
        // Main Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .pointerInput(state.isRunning, state.isPaused, state.isFinished) {
                    detectTapGestures(
                        onLongPress = {
                            context.performStrongVibration()       // «сильная» вибрация
                            onEvent(TimerEvent.OnPauseClick)
                        },
                        onTap = {
                            context.performStrongVibration(
                                durationMs = 120,
                                amplitude = 100   // заметно мягче, если устройство поддерживает амплитуду
                            )
                            onEvent(TimerEvent.OnTap)
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            MainTimerButton(state, screenHeightDp)
        }
        // Разделители
        DialDivider(angleDegrees = 0, color = Color(0x80000000))
        DialDivider(angleDegrees = 300, color = Color(0xFFCDDC39), alpha = 0.5f) // 10 sec
        DialDivider(angleDegrees = 240, color = Color(0xFFCDDC39), alpha = 0.5f) // 20 sec
        DialDivider(angleDegrees = 180, color = Color(0xFF3D5AFE))  // 30 sec
        DialDivider(angleDegrees = 120, color = Color(0xFFCDDC39), alpha = 0.5f)  // 40 sec
        DialDivider(angleDegrees = 60, color = Color(0x80fc520d))   // 50 sec
    }
}




@Preview(showBackground = true)
@Composable
private fun TimerAssemblyPreview() {
    TimerAssembly(
        state = TimerState(
            progressFraction = 0.5f,
            isRunning = true,
            isPaused = false,
            isFinished = false
        ),
        onEvent = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun TimerAssemblyFullPreview() {
    TimerAssembly(
        state = TimerState(
            progressFraction = 0.0f,
            isRunning = false,
            isPaused = false,
            isFinished = false
        ),
        onEvent = {}
    )
}