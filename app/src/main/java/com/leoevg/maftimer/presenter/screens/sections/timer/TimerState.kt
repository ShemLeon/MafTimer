package com.leoevg.maftimer.presenter.screens.sections.timer

data class TimerState(
    val totalSeconds: Int = 60,
    val progressFraction: Float = 0f, // от 0 до 1
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val isFinished: Boolean = false,
    val remainingSeconds: Int = 0
)

// TODO - вынести в CustomTimerState
//{
//    val remainingSeconds: Int
//        get() = (totalSeconds - (progressFraction * totalSeconds))
//            .toInt()
//            .coerceAtLeast(0)
//
//    val isFinished: Boolean
//        get() = progressFraction >= 1f
//}

/***
val remainingSeconds: Int - Производное (вычисляемое) свойство с кастомным getter’ом.
Хранится не в памяти — считается при каждом чтении

.coerceAtLeast(0) - Гарантируем, что не уйдет в минус из‑за погрешностей (минимум 0).

get() = progressFraction >= 1f - Завершено, когда доля достигла 1f.
 */