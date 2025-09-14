package com.leoevg.maftimer.presenter.screens.sections.player

import android.content.Context
import android.content.Intent
import android.util.Log
import com.leoevg.maftimer.presenter.util.Logx
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoevg.maftimer.data.repository.SpotifyRepository
import com.leoevg.maftimer.presenter.util.SpotifyAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val spotifyRepository: SpotifyRepository,
    private val authManager: SpotifyAuthManager,
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    companion object {        private const val TAG = "MusicPlayerViewModel"    }
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
            is MusicPlayerEvent.OnOverlayClicked -> {
                Logx.debug(TAG, "OnOverlayClicked event received")
                openSpotifyApp()
            }
            is MusicPlayerEvent.OnCheckAuthorization -> {
                Logx.debug(TAG, "OnCheckAuthorization event received")
                checkAuthorizationAndRefresh()
            }
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
                if (playback.isSuccess) {
                    val data = playback.getOrNull()
                    if (data == null) {
                        Logx.info(TAG, "Playback is null (likely 204) → show overlay")
                        _state.update {
                            it.copy(
                                isAuthorized = false,
                                isLoading = false,
                                isPlaying = false,
                                progressMs = 0,
                                durationMs = 0,
                                title = "",
                                artist = "",
                                albumCoverUrl = ""
                            )
                        }
                        return@launch
                    }
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isPlaying = data.isPlaying ?: false,
                            progressMs = data.progressMs ?: 0,
                            durationMs = data.item?.durationMs ?: 0,
                            title = data.item?.name ?: "",
                            artist = data.item?.artists?.getOrNull(0)?.name ?: "",
                            albumCoverUrl = data.item?.album?.images?.getOrNull(0)?.url ?: ""
                        )
                    }
                } else {
                    val msg = playback.exceptionOrNull()?.message.orEmpty()
                    if (msg.contains("401")) {
                        authManager.clearToken()
                        _state.update { it.copy(isAuthorized = false, isLoading = false, error = null) }
                    } else {
                        _state.update { it.copy(error = "Ошибка получения состояния проигрывания", isLoading = false) }
                    }
                }
            } catch (e: Exception) {
                val msg = e.message.orEmpty()
                if (msg.contains("401")) {
                    authManager.clearToken()
                    _state.update { it.copy(isAuthorized = false, isLoading = false, error = null) }
                } else {
                    _state.update { it.copy(error = e.message, isLoading = false) }
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun openSpotifyApp() {
        Logx.action(TAG, "openSpotifyApp")
        val launchIntent = appContext.packageManager.getLaunchIntentForPackage("com.spotify.music")
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            appContext.startActivity(launchIntent)
        }
    }

    private fun checkAuthorizationAndRefresh() {
        Logx.action(TAG, "checkAuthorizationAndRefresh")
        viewModelScope.launch {
            val token = authManager.getStoredToken()
            val isAuthorized = token != null
            Logx.info(TAG, "Authorization check: token=${if (token != null) "found" else "null"}, isAuthorized=$isAuthorized")
            _state.update { it.copy(isAuthorized = isAuthorized) }
            if (isAuthorized) {
                Logx.success(TAG, "Token found → refreshing playback")
                refreshPlayback()
            }
        }
    }

    fun updateSelectedPage(newPage: Int) {
        _state.update { it.copy(selectedPage = newPage) }  // Update state
    }
}