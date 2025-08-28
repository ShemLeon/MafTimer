package com.leoevg.maftimer.presenter.screens.main


import androidx.lifecycle.ViewModel
import android.os.SystemClock
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainScreenViewModel : ViewModel() {
    private val _progressFraction = MutableStateFlow(0f) // 0f..1f
    val progressFraction: StateFlow<Float> = _progressFraction.asStateFlow()
    private var timerJob: Job? = null // корутина

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.OnBtnTimerStartClick -> startTimer()
            is MainScreenEvent.OnBtnTimerStopClick -> stopTimer()
            is MainScreenEvent.OnBtnTimerResetClick -> resetTimer()
        }
    }

    private fun startTimer() {
        if (timerJob?.isActive == true || _progressFraction.value >= 1f) return
        val startFraction = _progressFraction.value
        val durationMs = ((1f - startFraction) * 60_000L).toLong().coerceAtLeast(1L)

        timerJob = viewModelScope.launch {
            val start = SystemClock.elapsedRealtime()
            val end = start + durationMs
            while (isActive) {
                val now = SystemClock.elapsedRealtime()
                val t = ((now - start).toFloat() / durationMs).coerceIn(0f, 1f)
                _progressFraction.value = startFraction + t * (1f - startFraction)
                if (now >= end) break
                delay(16) // ~60 FPS
            }
            _progressFraction.value = 1f
        }
    }
    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private fun resetTimer() {
        stopTimer()
        _progressFraction.value = 0f
    }


}