package com.leoevg.maftimer.presenter.screens.sections.timer

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class CustomTimerViewModel: ViewModel() {
    private var timerJob: Job? = null //корутина, которая будет работать в фоновом потоке
    private val _state = MutableStateFlow(CustomTimerState())
    val state: StateFlow<CustomTimerState> = _state.asStateFlow()

    fun onEvent(event: CustomTimerEvent) {
        when (event) {
            CustomTimerEvent.OnStartClick -> startTimer()
            CustomTimerEvent.OnResetClick -> resetTimer()
            CustomTimerEvent.OnPauseClick -> pauseTimer()
            CustomTimerEvent.OnResumeClick -> resumeTimer()
        }
    }

    private fun startTimer(){
        if (timerJob?.isActive == true) return
        // случай, когда предыдущий таймер отработал свой круг
        if (_state.value.isFinished)
            _state.value = _state.value.copy(progressFraction = 0f, isPaused = false)
        // основное событие - вначале устанавливаем isRunning = true
        _state.value = _state.value.copy(isRunning = true, isPaused = false)

        val startFraction = _state.value.progressFraction
        val totalSeconds = _state.value.totalSeconds
        val durationMs = ((1f - startFraction) * totalSeconds * 1000).toLong().coerceAtLeast(1L)

        timerJob = viewModelScope.launch {
            val start = SystemClock.elapsedRealtime()
            val end = start + durationMs
            while (isActive) {
                val now = SystemClock.elapsedRealtime()
                val t = ((now - start).toFloat() / durationMs).coerceIn(0f, 1f)
            }
        }
    }
    private fun resetTimer(){

    }
    private fun pauseTimer(){

    }

    private fun resumeTimer(){

    }
}


