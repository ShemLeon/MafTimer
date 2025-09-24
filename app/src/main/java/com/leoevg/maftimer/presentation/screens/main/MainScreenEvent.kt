package com.leoevg.maftimer.presentation.screens.main

sealed interface MainScreenEvent {
    object OnSpotifyAuthRequest: MainScreenEvent
}