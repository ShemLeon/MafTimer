package com.leoevg.maftimer.presenter.screens.sections.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoevg.maftimer.data.repository.SpotifyRepository
import com.leoevg.maftimer.util.SpotifyAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val spotifyRepository: SpotifyRepository,
    private val authManager: SpotifyAuthManager
) : ViewModel() {
    private val _state = MutableStateFlow(MusicPlayerState())
    // исправится само когда добавим параметры по умолчанию
    val state: StateFlow<MusicPlayerState> = _state.asStateFlow()


    init {
        // Устанавливаем токен при инициализации
        authManager.getStoredToken()?.let { token ->
            spotifyRepository.setAccessToken(token)
        }
    }

    fun sendEvent(event: MusicPlayerEvent){
        // Todo: обработать евент по шаблону
    }

    fun play() {
        viewModelScope.launch {
            spotifyRepository.play()
        }
    }

    fun pause() {
        viewModelScope.launch {
            spotifyRepository.pause()
        }
    }

    fun next() {
        viewModelScope.launch {
            spotifyRepository.next()
        }
    }

    fun previous() {
        viewModelScope.launch {
            spotifyRepository.previous()
        }
    }

    fun seekTo(positionMs: Long) {
        viewModelScope.launch {
            spotifyRepository.seekTo(positionMs)
        }
    }

    fun refreshPlayback() {
        viewModelScope.launch {
            spotifyRepository.getCurrentPlayback()
        }
    }

    fun isAuthorized(): Boolean {
        return authManager.getStoredToken() != null
    }
}