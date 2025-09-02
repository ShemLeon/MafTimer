package com.leoevg.maftimer.presenter.screens.sections.timer

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
        // 1. Если корутина активна - защита от повторного запуска
        if (timerJob?.isActive == true) return
        // 2. Если таймер завершил свою работу (isFinished = true), сбрасываем прогресс на 0
        // Используется метод copy() для создания нового состояния с измененными полями
        if (_state.value.isFinished)
            _state.value = _state.value.copy(progressFraction = 0f, isPaused = false)
        // 3. Установка состояния запуска: таймер запущен и не на паузе
        _state.value = _state.value.copy(isRunning = true, isPaused = false)
        // 4. Расчет времени
        val startFraction = _state.value.progressFraction   // текущий прогресс (от 0 до 1)
        val totalSeconds = _state.value.totalSeconds        // общее время таймера в секундах (60)
        val durationMs = ((1f - startFraction) * totalSeconds * 1000).toLong().coerceAtLeast(1L) // Оставшееся время в миллисекундах
        // 5. Запуск таймера
        timerJob = viewModelScope.launch {
            val start = SystemClock.elapsedRealtime() // время в мс с момента запуска
            val end = start + durationMs
            while (isActive) {
                val now = SystemClock.elapsedRealtime()
                val t = ((now - start).toFloat() / durationMs).coerceIn(0f, 1f)
                _state.value = _state.value.copy(
                    progressFraction = startFraction + t *(1f-startFraction)
                )
                if (now >= end) {
                    _state.value = _state.value.copy(
                        isFinished = true,
                        isRunning = false
                    )
                    break
                }
                delay(8) // ~120 FPS
            }
            // Устанавливаем isRunning = false когда таймер завершен
            _state.value = _state.value.copy(isRunning = false)
        }
    }

    private fun resetTimer(){

    }
    private fun pauseTimer(){
        if (_state.value.isRunning && !_state.value.isPaused) {
            timerJob?.cancel()
            timerJob = null
            _state.value = _state.value.copy(isRunning = false, isPaused = true)
        }
    }

    private fun resumeTimer(){

    }
}


