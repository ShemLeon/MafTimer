package com.leoevg.maftimer.presentation.screens.main

import androidx.lifecycle.ViewModel
import com.leoevg.maftimer.MainActivity
import com.leoevg.maftimer.data.repository.SpotifyRepository
import com.leoevg.maftimer.presentation.util.Logx
import com.leoevg.maftimer.presentation.util.SpotifyAuthManager
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

    companion object {
        private const val TAG = "MainScreenViewModel"
    }
}