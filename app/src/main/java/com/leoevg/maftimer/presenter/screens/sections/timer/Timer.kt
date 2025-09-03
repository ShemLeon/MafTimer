package com.leoevg.maftimer.presenter.screens.sections.timer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.leoevg.maftimer.presenter.screens.sections.timer.components.TimerAssembly
import kotlin.Int

@Composable
fun Timer(
    state: TimerState,
    onEvent: (TimerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    TimerAssembly(
        state = state,
        onEvent = onEvent,
        modifier = modifier
    )
}
//    val viewModel: ViewModel = TimerViewModel()
//    val state by viewModel.state.collectAsState()

@Preview(showBackground = true)
@Composable
fun TimerPreview() {
    Timer(
        state = TimerState(
            totalSeconds = 60,
            progressFraction = 0.3f, // от 0 до 1
            isRunning = false,
            isPaused = false,
            isFinished = false
    ),
        onEvent =  {}
    )
}

