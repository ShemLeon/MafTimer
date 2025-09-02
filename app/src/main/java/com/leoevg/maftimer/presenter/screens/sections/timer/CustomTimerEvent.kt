package com.leoevg.maftimer.presenter.screens.sections.timer

sealed interface CustomTimerEvent {
    object OnStartClick: CustomTimerEvent
    object OnResetClick: CustomTimerEvent
    object OnPauseClick: CustomTimerEvent
    object OnResumeClick: CustomTimerEvent
}