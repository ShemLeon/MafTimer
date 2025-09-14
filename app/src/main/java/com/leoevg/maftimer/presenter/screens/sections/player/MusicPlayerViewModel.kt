package com.leoevg.maftimer.presenter.screens.sections.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoevg.maftimer.presenter.screens.sections.player.local.ILocalPlayerController
import com.leoevg.maftimer.presenter.screens.sections.player.local.LocalPlayback
import com.leoevg.maftimer.presenter.screens.sections.player.spotify.ISpotifyPlaybackController
import com.leoevg.maftimer.presenter.screens.sections.player.spotify.RemotePlayback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val spotify: ISpotifyPlaybackController,
    private val local: ILocalPlayerController
) : ViewModel() {

    private val _state = MutableStateFlow(MusicPlayerState())
    val state: StateFlow<MusicPlayerState> = _state.asStateFlow()

    private var localProgressJob: Job? = null

    init {
        // Restore Spotify token and set initial auth flag
        spotify.setAccessFromStored()
        _state.update { it.copy(isAuthorized = spotify.isAuthorized()) }

        // Observe local player and reduce to UI state
        viewModelScope.launch {
            local.state.collect { lp ->
                _state.update { MusicPlayerStateReducer.withLocal(it, lp) }
                toggleLocalProgressJob(lp)
            }
        }
    }

    fun sendEvent(event: MusicPlayerEvent) {
        val isLocalPage = state.value.selectedPage == 0
        when (event) {
            is MusicPlayerEvent.OnSpotifyAuthRequest -> checkAuthorization()

            is MusicPlayerEvent.OnStartBtnClicked -> {
                if (isLocalPage) {
                    if (state.value.isLocalLoaded) local.play()
                } else {
                    if (state.value.isAuthorized) viewModelScope.launch { spotify.play() }
                }
            }

            is MusicPlayerEvent.OnPauseBtnClicked -> {
                if (isLocalPage) {
                    if (state.value.isLocalLoaded) local.pause()
                } else {
                    if (state.value.isAuthorized) viewModelScope.launch { spotify.pause() }
                }
            }

            is MusicPlayerEvent.OnNextSongBtnClicked -> {
                if (isLocalPage) {
                    if (state.value.isLocalLoaded) local.next()
                } else {
                    if (state.value.isAuthorized) viewModelScope.launch { spotify.next() }
                }
            }

            is MusicPlayerEvent.OnPreviousSongBtnClicked -> {
                if (isLocalPage) {
                    if (state.value.isLocalLoaded) local.previous()
                } else {
                    if (state.value.isAuthorized) viewModelScope.launch { spotify.previous() }
                }
            }

            is MusicPlayerEvent.OnSeekTo -> {
                if (isLocalPage) {
                    if (state.value.isLocalLoaded) local.seekTo(event.positionMs)
                } else {
                    if (state.value.isAuthorized) viewModelScope.launch { spotify.seekTo(event.positionMs) }
                }
            }

            is MusicPlayerEvent.OnRefreshPlayback -> {
                if (isLocalPage) {
                    local.refresh()
                } else {
                    refreshRemote()
                }
            }

            is MusicPlayerEvent.OnOverlayClicked -> {
                if (!isLocalPage) spotify.openSpotifyApp()
            }

            is MusicPlayerEvent.OnCheckAuthorization -> {
                checkAuthorizationAndMaybeRefresh()
            }
        }
    }

    fun updateSelectedPage(newPage: Int) {
        _state.update { it.copy(selectedPage = newPage) }
        if (newPage == 0) {
            local.refresh()
        } else {
            if (spotify.isAuthorized()) {
                refreshRemote()
            } else {
                _state.update { it.copy(isAuthorized = false) }
            }
        }
    }

    // Called from UI after Android 13+ permission is granted
    fun onLocalPermissionGranted() {
        viewModelScope.launch { local.initLibrary() }
    }

    // Backward compatibility (overlay logic), reducer will overwrite with real readiness
    fun setLocalLoaded(loaded: Boolean) {
        _state.update { it.copy(isLocalLoaded = loaded) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun checkAuthorization() {
        _state.update { it.copy(isAuthorized = spotify.isAuthorized()) }
    }

    private fun checkAuthorizationAndMaybeRefresh() {
        val authorized = spotify.isAuthorized()
        _state.update { it.copy(isAuthorized = authorized) }
        if (authorized) refreshRemote()
    }

    private fun refreshRemote() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = spotify.getPlayback()
            result.onSuccess { rp: RemotePlayback? ->
                _state.update { st ->
                    MusicPlayerStateReducer.withRemote(st.copy(isLoading = false, error = null), rp)
                }
            }.onFailure { t ->
                val msg = t.message.orEmpty()
                if (msg.contains("401")) {
                    spotify.clearAuthOn401()
                    _state.update { it.copy(isAuthorized = false, isLoading = false, error = null) }
                } else {
                    _state.update { it.copy(isLoading = false, error = msg) }
                }
            }
        }
    }

    private fun toggleLocalProgressJob(lp: LocalPlayback) {
        if (lp.isPlaying && localProgressJob == null) {
            localProgressJob = viewModelScope.launch {
                while (true) {
                    local.refresh()
                    delay(500L)
                }
            }
        } else if (!lp.isPlaying && localProgressJob != null) {
            localProgressJob?.cancel()
            localProgressJob = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        localProgressJob?.cancel()
        local.release()
    }
}