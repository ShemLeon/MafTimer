package com.leoevg.maftimer.presenter.screens.main

sealed interface MainScreenEvent {
    object OnBtnTimerStartClick: MainScreenEvent
    object OnBtnTimerStopClick: MainScreenEvent
    object OnBtnTimerResetClick: MainScreenEvent
    object OnBtnTimerPauseClick: MainScreenEvent
}