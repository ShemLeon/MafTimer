package com.leoevg.maftimer.presenter.screens.sections.player.local

import android.content.Context
import androidx.media3.common.Player
import com.leoevg.maftimer.data.Song
import com.leoevg.maftimer.data.getSongs
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class LocalPlayerControllerImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ILocalPlayerController {
    private val exo = ExoPlayer.Builder(appContext).build()
    private val _state = MutableStateFlow(LocalPlayback())
    override val state: StateFlow<LocalPlayback> = _state.asStateFlow()

    private var songs: List<Song> = emptyList()
    private var index: Int = 0

    private val listener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _state.value = _state.value.copy(isPlaying = isPlaying)
        }
        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                _state.value = _state.value.copy(
                    isReady = true,
                    durationMs = exo.duration.coerceAtLeast(0L)
                )
            }
            if (playbackState == Player.STATE_ENDED) next()
        }
    }

    override suspend fun initLibrary() {
        songs = getSongs(appContext)
        exo.addListener(listener)
        _state.value = _state.value.copy(isReady = songs.isNotEmpty())
        if (songs.isNotEmpty()) {
            index = 0
            prepareAndPlay(index, play = false)
        }
    }

    override fun play() {
        if (exo.playbackState == Player.STATE_IDLE && songs.isNotEmpty()) {
            prepareAndPlay(index, play = true)
        } else exo.play()
    }

    override fun pause() { exo.pause() }

    override fun next() {
        if (songs.isEmpty()) return
        index = (index + 1) % songs.size
        prepareAndPlay(index, play = true)
    }

    override fun previous() {
        if (songs.isEmpty()) return
        index = if (index - 1 < 0) songs.size - 1 else index - 1
        prepareAndPlay(index, play = true)
    }

    override fun seekTo(positionMs: Long) {
        exo.seekTo(positionMs)
        _state.value = _state.value.copy(positionMs = positionMs)
    }

    override fun refresh() {
        _state.value = _state.value.copy(
            isPlaying = exo.isPlaying,
            durationMs = exo.duration.coerceAtLeast(0L),
            positionMs = exo.currentPosition.coerceAtLeast(0L),
            currentIndex = index,
            currentSong = songs.getOrNull(index)
        )
    }

    private fun prepareAndPlay(i: Int, play: Boolean) {
        val s = songs.getOrNull(i) ?: return
        exo.setMediaItem(MediaItem.fromUri(s.data))
        exo.prepare()
        exo.playWhenReady = play
        _state.value = _state.value.copy(
            currentIndex = i,
            currentSong = s,
            positionMs = 0L
        )
    }

    override fun release() {
        exo.removeListener(listener)
        exo.release()
    }
}