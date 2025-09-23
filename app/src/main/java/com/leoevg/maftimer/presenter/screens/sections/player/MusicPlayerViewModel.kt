package com.leoevg.maftimer.presenter.screens.sections.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoevg.maftimer.presenter.screens.sections.player.local.ILocalPlayerController
import com.leoevg.maftimer.presenter.screens.sections.player.local.LocalPlayback
import com.leoevg.maftimer.presenter.screens.sections.player.spotify.ISpotifyPlaybackController
import com.leoevg.maftimer.presenter.screens.sections.player.spotify.RemotePlayback
import com.leoevg.maftimer.presenter.util.Logx
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

    // Periodic pull for local progress to keep UI smooth
    private var localProgressJob: Job? = null

    init {
        // Restore token to repository and set initial auth flag
        spotify.setAccessFromStored()
        val authorized = spotify.isAuthorized()
        _state.update {
            it.copy(
                isAuthorizedSpotify = authorized,
                showSpotifyOverlay = !authorized  // Показывать оверлей только если НЕ авторизован
            )
        }

        // Observe local player state and reduce it into UI state
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
                    if (state.value.isAuthorizedSpotify) viewModelScope.launch { spotify.play() }
                }
            }

            is MusicPlayerEvent.OnPauseBtnClicked -> {
                if (isLocalPage) {
                    if (state.value.isLocalLoaded) local.pause()
                } else {
                    if (state.value.isAuthorizedSpotify) viewModelScope.launch { spotify.pause() }
                }
            }

            is MusicPlayerEvent.OnNextSongBtnClicked -> {
                if (isLocalPage) {
                    if (state.value.isLocalLoaded) local.next()
                } else {
                    if (state.value.isAuthorizedSpotify) viewModelScope.launch { spotify.next() }
                }
            }

            is MusicPlayerEvent.OnPreviousSongBtnClicked -> {
                if (isLocalPage) {
                    if (state.value.isLocalLoaded) local.previous()
                } else {
                    if (state.value.isAuthorizedSpotify) viewModelScope.launch { spotify.previous() }
                }
            }

            is MusicPlayerEvent.OnSeekTo -> {
                if (isLocalPage) {
                    if (state.value.isLocalLoaded) local.seekTo(event.positionMs)
                } else {
                    if (state.value.isAuthorizedSpotify) viewModelScope.launch {
                        spotify.seekTo(
                            event.positionMs
                        )
                    }
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
                if (!isLocalPage) {
                    spotify.openSpotifyApp()
                    _state.update {
                        it.copy(
                            showSpotifyOverlay = false,
                            spotIntentActivated = true
                        )
                    }
                }
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
            // When switching to Spotify page, ensure repo has the latest token and refresh
            spotify.setAccessFromStored()
            val authorized = spotify.isAuthorized()
            _state.update { it.copy(isAuthorizedSpotify = authorized) }
            if (authorized) refreshRemote()
        }
    }

    // Called by UI after Android 13+ READ_MEDIA_AUDIO is granted
    fun onLocalPermissionGranted() {
        viewModelScope.launch { local.initLibrary() }
    }

    fun hideAllOverlays() {
        Logx.info("MusicPlayerViewModel", "Hiding ALL overlays and resetting spotIntentActivated")
        _state.update {
            it.copy(
                showSpotifyOverlay = false,
                showLocalOverlay = false,
                spotIntentActivated = false
            )
        }
        // Принудительно проверяем авторизацию после скрытия оверлея
        checkAuthorizationAndMaybeRefresh()
    }

    fun hideSpotifyOverlayOnly() {
        Logx.info("MusicPlayerViewModel", "Hiding Spotify overlay only, keeping spotIntentActivated")
        _state.update {
            it.copy(
                showSpotifyOverlay = false
                // НЕ сбрасываем spotIntentActivated!
            )
        }
    }

    fun showAllOverlays() {
        val trace = Thread.currentThread().stackTrace
        val caller = trace.getOrNull(3)?.let { "${it.className}.${it.methodName}:${it.lineNumber}" } ?: "unknown"
        Logx.info("MusicPlayerViewModel", "🚨 showAllOverlays() called from: $caller")
        _state.update {
            it.copy(
                showSpotifyOverlay = if (it.isAuthorizedSpotify && it.selectedPage == 1) false else true,
                showLocalOverlay = true
                // НЕ сбрасываем spotIntentActivated здесь!
            )
        }
    }

    fun setSpotifyIntentActivated(activated: Boolean) {
        Logx.info("MusicPlayerViewModel", "🏷️ Setting spotIntentActivated=$activated")
        _state.update { it.copy(spotIntentActivated = activated) }
    }

    // Optional: keeps overlay logic backward compatible; reducer will overwrite when local becomes ready
    fun setLocalLoaded(loaded: Boolean) {
        _state.update { it.copy(isLocalLoaded = loaded) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun checkAuthorization() {
        _state.update { it.copy(isAuthorizedSpotify = spotify.isAuthorized()) }
    }

    // IMPORTANT: pick up new token on resume before checking auth
    private fun checkAuthorizationAndMaybeRefresh() {
        // Pull latest token from storage into repository/controller
        spotify.setAccessFromStored()

        val authorized = spotify.isAuthorized()
        _state.update { it.copy(isAuthorizedSpotify = authorized) }
        if (authorized) refreshRemote()
    }

    private fun refreshRemote() {
        viewModelScope.launch {
            Logx.debug("MusicPlayerViewModel", "🔄 refreshRemote() started")
            _state.update { it.copy(isLoading = true) }
            val result = spotify.getPlayback()
            result.onSuccess { rp: RemotePlayback? ->
                Logx.debug("MusicPlayerViewModel", "✅ refreshRemote() success - preserving overlay state")
                val currentOverlayState = _state.value.showSpotifyOverlay
                val currentSpotIntentState = _state.value.spotIntentActivated
                _state.update { st ->
                    MusicPlayerStateReducer.withRemote(st.copy(isLoading = false, error = null), rp).copy(
                        showSpotifyOverlay = currentOverlayState,
                        spotIntentActivated = currentSpotIntentState
                    )
                }
            }.onFailure { t ->
                val msg = t.message.orEmpty()
                if (msg.contains("401")) {
                    spotify.clearAuthOn401()
                    _state.update {
                        it.copy(
                            isAuthorizedSpotify = false,
                            isLoading = false,
                            error = null
                        )
                    }
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