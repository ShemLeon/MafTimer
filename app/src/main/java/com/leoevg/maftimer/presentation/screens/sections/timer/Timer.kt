package com.leoevg.maftimer.presentation.screens.sections.timer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.maftimer.presentation.screens.sections.timer.components.TimerAssembly

@Composable
fun Timer(
    modifier: Modifier = Modifier,
    viewModel: TimerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    TimerAssembly(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun TimerPreview() {
    Timer()
}

//@Preview(showBackground = true)
//@Composable
//fun TimerPreview() {
//    Timer(
//        state = TimerState(
//            totalSeconds = 60,
//            progressFraction = 0.3f, // от 0 до 1
//            isRunning = false,
//            isPaused = false,
//            isFinished = false
//    ),
//        onEvent =  {}
//    )
//}

