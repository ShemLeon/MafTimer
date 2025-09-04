package com.leoevg.maftimer.presenter.screens.sections.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoevg.maftimer.data.repository.SpotifyRepository
import com.leoevg.maftimer.util.SpotifyAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val spotifyRepository: SpotifyRepository,
    private val authManager: SpotifyAuthManager
) : ViewModel() {
    private val _state = MutableStateFlow(MusicPlayerState())
    val state: StateFlow<MusicPlayerState> = _state.asStateFlow()
    // Устанавливаем токен при инициализации
    init {
        authManager.getStoredToken()?.let { token ->
            spotifyRepository.setAccessToken(token)
        }
    }

    fun sendEvent(event: MusicPlayerEvent) {
        when (event) {
            is MusicPlayerEvent.OnSpotifyAuthRequest -> checkAuthorization()
            is MusicPlayerEvent.OnStartBtnClicked -> play()
            is MusicPlayerEvent.OnPauseBtnClicked -> pause()
            is MusicPlayerEvent.OnNextSongBtnClicked -> next()
            is MusicPlayerEvent.OnPreviousSongBtnClicked -> previous()
            is MusicPlayerEvent.OnSeekTo -> seekTo(event.positionMs)
            is MusicPlayerEvent.OnRefreshPlayback -> refreshPlayback()
        }
    }

    private fun checkAuthorization() {
        val isAuthorized = authManager.getStoredToken() != null
        _state.update { it.copy(isAuthorized = isAuthorized) }
    }

    private fun play() {
        if (!state.value.isAuthorized) return // защита
        // отработка корутины проигрывания музыки
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                spotifyRepository.play()
                _state.update { it.copy(isPlaying = true, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    private fun pause() {
        if (!state.value.isAuthorized) return
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                spotifyRepository.pause()
                _state.update { it.copy(isPlaying = false, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    private fun next() {
        if (!state.value.isAuthorized) return
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                spotifyRepository.next()
                refreshPlayback()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }


    private fun previous() {
        if (!state.value.isAuthorized) return

        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                spotifyRepository.previous()
                refreshPlayback()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    private fun seekTo(positionMs: Long) {
        if (!state.value.isAuthorized) return

        viewModelScope.launch {
            try {
                spotifyRepository.seekTo(positionMs)
                _state.update { it.copy(progressMs = positionMs) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }
    private fun refreshPlayback() {
        if (!state.value.isAuthorized) return

        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                val playback = spotifyRepository.getCurrentPlayback()
                // Здесь нужно обновить состояние на основе полученных данных
                // _state.update { it.copy(...) }
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }


}