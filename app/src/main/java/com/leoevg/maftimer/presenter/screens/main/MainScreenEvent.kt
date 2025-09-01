package com.leoevg.maftimer.presenter.screens.main

sealed interface MainScreenEvent {
    object OnStartClick: MainScreenEvent
    object OnStopClick: MainScreenEvent
    object OnResetClick: MainScreenEvent
    object OnPauseClick: MainScreenEvent
    object OnResumeClick: MainScreenEvent
    object OnSpotifyAuthRequest: MainScreenEvent
}