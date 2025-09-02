package com.leoevg.maftimer.presenter.screens.sections.timer

sealed interface TimerEvent {
    object OnStartClick: TimerEvent
    object OnResetClick: TimerEvent
    object OnPauseClick: TimerEvent
    object OnResumeClick: TimerEvent
}