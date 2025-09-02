package com.leoevg.maftimer.presenter.screens.main

sealed interface MainScreenEvent {
    object OnSpotifyAuthRequest: MainScreenEvent
}