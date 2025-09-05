package com.leoevg.maftimer.presenter.screens.sections.timer.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.R
import com.leoevg.maftimer.presenter.screens.sections.timer.TimerState

@Composable
fun MainTimerButton(
    state: TimerState = TimerState(),
    screenHeightDp: Dp = 100.dp
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
            .offset(
                x =
                    if (state.isRunning || state.isPaused) 0.dp
                    else (screenHeightDp * 0.015f)
            )
    )
}

@Preview(showBackground = true, name = "Start State")
@Composable
private fun MainTimerButtonStartPreview() {
    MainTimerButton(
        state = TimerState(
            isRunning = false,
            isPaused = false,
            isFinished = false
        ),
        screenHeightDp = 700.dp
    )
}


@Preview(showBackground = true, name = "Running State")
@Composable
private fun MainTimerButtonRunningPreview() {
    MainTimerButton(
        state = TimerState(
            isRunning = true,
            isPaused = false,
            isFinished = false
        ),
        screenHeightDp = 700.dp
    )
}

@Preview(showBackground = true, name = "Paused State")
@Composable
private fun MainTimerButtonPausedPreview() {
    MainTimerButton(
        state = TimerState(
            isRunning = false,
            isPaused = true,
            isFinished = false
        ),
        screenHeightDp = 400.dp
    )
}
