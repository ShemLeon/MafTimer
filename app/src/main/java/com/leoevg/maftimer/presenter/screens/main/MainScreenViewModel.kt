package com.leoevg.maftimer.presenter.screens.main


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {
    // Здесь будет только логика для управления главным экраном
    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.OnSpotifyAuthRequest -> TODO()
        }
    }

}