package com.leoevg.maftimer.presenter.screens.sections.player

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.leoevg.maftimer.data.Song
import com.leoevg.maftimer.data.getSongs
import com.leoevg.maftimer.data.repository.SpotifyRepository
import com.leoevg.maftimer.presenter.util.Logx
import com.leoevg.maftimer.presenter.util.SpotifyAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    private val spotifyRepository: SpotifyRepository,
    private val authManager: SpotifyAuthManager,
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    companion object {private const val TAG = "MusicPlayerViewModel"}
    private val _state = MutableStateFlow(MusicPlayerState())
    val state: StateFlow<MusicPlayerState> = _state.asStateFlow()
    // Local (Media3/ExoPlayer)
    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(appContext).build()
    private var localSongs: List<Song> = emptyList()
    private var localCurrentIndex: Int = 0
    private var progressJob: Job? = null

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlay: Boolean) {
            _state.update { it.copy(isPlaying = isPlay) }
            if (isPlay) startProgressUpdates() else stopProgressUpdates()
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                _state.update { it.copy(durationMs = exoPlayer.duration.coerceAtLeast(0L)) }
            }
            if (playbackState == Player.STATE_ENDED) {
                nextLocal()
            }
        }
    }

    init {
        // Restore Spotify token
        authManager.getStoredToken()?.let { token ->
            spotifyRepository.setAccessToken(token)
        }
        exoPlayer.addListener(playerListener)
    }


    fun sendEvent(event: MusicPlayerEvent) {
        val isLocal = state.value.selectedPage == 0
        when (event) {
            is MusicPlayerEvent.OnSpotifyAuthRequest -> checkAuthorization()
            is MusicPlayerEvent.OnStartBtnClicked -> {
                if (isLocal) {
                    if (state.value.isLocalLoaded) playLocal()
                } else {
                    if (state.value.isAuthorized) play()
                }
            }
            is MusicPlayerEvent.OnPauseBtnClicked -> {
                if (isLocal) {
                    if (state.value.isLocalLoaded) pauseLocal()
                } else {
                    if (state.value.isAuthorized) pause()
                }
            }
            is MusicPlayerEvent.OnNextSongBtnClicked -> {
                if (isLocal) {
                    if (state.value.isLocalLoaded) nextLocal()
                } else {
                    if (state.value.isAuthorized) next()
                }
            }

            is MusicPlayerEvent.OnNextSongBtnClicked -> {
                if (isLocal) {
                    if (state.value.isLocalLoaded) nextLocal()
                } else {
                    if (state.value.isAuthorized) next()
                }
            }

            is MusicPlayerEvent.OnPreviousSongBtnClicked -> {
                if (isLocal) {
                    if (state.value.isLocalLoaded) previousLocal()
                } else {
                    if (state.value.isAuthorized) previous()
                }
            }

            is MusicPlayerEvent.OnSeekTo -> {
                if (isLocal) {
                    if (state.value.isLocalLoaded) seekLocal(event.positionMs)
                } else {
                    if (state.value.isAuthorized) seekTo(event.positionMs)
                }
            }

            is MusicPlayerEvent.OnRefreshPlayback -> {
                if (isLocal) refreshLocal() else refreshPlayback()
            }

            is MusicPlayerEvent.OnOverlayClicked -> {
                // Open Spotify app if not local page
                if (!isLocal) openSpotifyApp()
            }

            is MusicPlayerEvent.OnCheckAuthorization -> {
                checkAuthorizationAndRefresh()
            }

        }
    }

    // ---- Spotify (remote) ----
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
        _state.update { it.copy(selectedPage = newPage) }  // This syncs state, fixing indicators not reacting
    }

    fun setLocalLoaded(loaded: Boolean) {
        _state.update { it.copy(isLocalLoaded = loaded) }  // Update when local files loaded/permissions granted
    }

    // Called from UI after the permission is granted
    fun onLocalPermissionGranted() {
        initLocalPlayer()
        setLocalLoaded(true)
    }

    // ---- Local (Media3/ExoPlayer) ----

    private fun initLocalPlayer() {
        viewModelScope.launch {
            localSongs = getSongs(appContext)
            _state.update { it.copy(isLocalLoaded = localSongs.isNotEmpty()) }
            if (localSongs.isNotEmpty()) {
                localCurrentIndex = 0
                prepareAndPlayLocal(localCurrentIndex, play = false)
            }
        }
    }

    private fun prepareAndPlayLocal(index: Int, play: Boolean = true) {
        val song = localSongs.getOrNull(index) ?: return
        exoPlayer.setMediaItem(MediaItem.fromUri(song.data))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = play
        _state.update {
            it.copy(
                title = song.title ?: "",
                artist = song.artist ?: "",
                progressMs = 0L
            )
        }
    }

    private fun playLocal() {
        if (localSongs.isEmpty()) return
        if (exoPlayer.playbackState == Player.STATE_IDLE) {
            prepareAndPlayLocal(localCurrentIndex, play = true)
        } else {
            exoPlayer.play()
        }
    }

    private fun pauseLocal() {
        exoPlayer.pause()
    }

    private fun nextLocal() {
        if (localSongs.isEmpty()) return
        localCurrentIndex = (localCurrentIndex + 1) % localSongs.size
        prepareAndPlayLocal(localCurrentIndex, play = true)
    }

    private fun previousLocal() {
        if (localSongs.isEmpty()) return
        localCurrentIndex = if (localCurrentIndex - 1 < 0) localSongs.size - 1 else localCurrentIndex - 1
        prepareAndPlayLocal(localCurrentIndex, play = true)
    }

    private fun seekLocal(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
        _state.update { it.copy(progressMs = positionMs) }
    }

    private fun refreshLocal() {
        if (localSongs.isEmpty()) return
        val song = localSongs.getOrNull(localCurrentIndex)
        _state.update {
            it.copy(
                title = song?.title.orEmpty(),
                artist = song?.artist.orEmpty(),
                durationMs = exoPlayer.duration.coerceAtLeast(0L),
                progressMs = exoPlayer.currentPosition.coerceAtLeast(0L),
                isPlaying = exoPlayer.isPlaying
            )
        }
    }

    private fun startProgressUpdates() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (true) {
                _state.update { it.copy(progressMs = exoPlayer.currentPosition.coerceAtLeast(0L)) }
                delay(500)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopProgressUpdates()
        exoPlayer.removeListener(playerListener)
        exoPlayer.release()
    }

}