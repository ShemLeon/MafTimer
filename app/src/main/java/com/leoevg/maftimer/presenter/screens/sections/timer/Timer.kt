package com.leoevg.maftimer.presenter.screens.sections.timer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.lifecycle.ViewModel
import com.leoevg.maftimer.presenter.screens.sections.timer.components.TimerAssembly

@Composable
fun Timer(
    state: TimerState,
    onEvent: (TimerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: ViewModel = TimerViewModel()
    val state by viewModel.state.collectAsState()

    val hapticFeedback = LocalHapticFeedback.current   // вибрация при нажатии
    val context = LocalContext.current

    TimerAssembly(
        state = state,
        onEvent = viewModel::onEvent,
    )
}