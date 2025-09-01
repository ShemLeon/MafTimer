package com.leoevg.maftimer.presenter.screens.main


import androidx.lifecycle.ViewModel
import android.os.SystemClock
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {
    private var timerJob: Job? = null // корутина
    private val _state = MutableStateFlow(MainScreenState(totalSeconds = 60)) // потом поменять на 60
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    fun onEvent(event: MainScreenEvent) {

        when (event) {
            is MainScreenEvent.OnBtnTimerStartClick -> startTimer()
            is MainScreenEvent.OnBtnTimerStopClick -> stopTimer()
            is MainScreenEvent.OnBtnTimerResetClick -> resetTimer()
            is MainScreenEvent.OnBtnTimerPauseClick -> pauseTimer()
            is MainScreenEvent.OnBtnTimerResumeClick -> resumeTimer()
        }
    }

    private fun startTimer() {
  //    android.util.Log.d("GsonTest", com.leoevg.maftimer.presenter.util.GsonTest.testGson())

        if (timerJob?.isActive == true) return
        if (_state.value.isFinished){
            _state.value = _state.value.copy(progressFraction = 0f, isPaused = false)
        }
        // Устанавливаем isRunning = true в начале
        _state.value = _state.value.copy(isRunning = true,  isPaused = false)

        val startFraction = _state.value.progressFraction
        val totalSeconds = _state.value.totalSeconds
        val durationMs = ((1f - startFraction) * totalSeconds * 1000L).toLong().coerceAtLeast(1L)

        timerJob = viewModelScope.launch {
            val start = SystemClock.elapsedRealtime()
            val end = start + durationMs
            while (isActive) {
                val now = SystemClock.elapsedRealtime()
                val t = ((now - start).toFloat() / durationMs).coerceIn(0f, 1f)
                _state.value = _state.value.copy(
                    progressFraction = startFraction + t * (1f - startFraction)
                )
                if (now >= end) break
                delay(8) // ~60 FPS
            }
            // Устанавливаем isRunning = false когда таймер завершен
            _state.value = _state.value.copy(isRunning = false)
        }
    }

    private fun pauseTimer() {
        if (_state.value.isRunning && !_state.value.isPaused) {
            timerJob?.cancel()
            timerJob = null
            _state.value = _state.value.copy(isRunning = false, isPaused = true)
        }
    }

    private fun resumeTimer() {
        if (_state.value.isPaused && !_state.value.isRunning) {
            _state.value = _state.value.copy(isPaused = false)
            startTimer() // Продолжаем с текущей позиции
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        _state.value = _state.value.copy(isRunning = false)
    }

    private fun resetTimer() {
        stopTimer()
        _state.value = _state.value.copy(progressFraction = 0f, isPaused = false)
    }


}