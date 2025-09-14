package com.leoevg.maftimer.presenter.screens.sections.player

import com.leoevg.maftimer.presenter.screens.sections.player.local.LocalPlayback
import com.leoevg.maftimer.presenter.screens.sections.player.spotify.RemotePlayback

object MusicPlayerStateReducer {
    fun withLocal(prev: MusicPlayerState, lp: LocalPlayback): MusicPlayerState =
        prev.copy(
            isLocalLoaded = lp.isReady,
            isPlaying = lp.isPlaying,
            progressMs = lp.positionMs,
            durationMs = lp.durationMs,
            title = lp.currentSong?.title ?: "",
            artist = lp.currentSong?.artist ?: ""
        )

    fun withRemote(prev: MusicPlayerState, rp: RemotePlayback?): MusicPlayerState =
        if (rp == null) prev.copy(
            isAuthorized = false,
            isPlaying = false,
            progressMs = 0,
            durationMs = 0,
            title = "",
            artist = "",
            albumCoverUrl = ""
        ) else prev.copy(
            isAuthorized = true,
            isPlaying = rp.isPlaying,
            progressMs = rp.progressMs,
            durationMs = rp.durationMs,
            title = rp.title,
            artist = rp.artist,
            albumCoverUrl = rp.albumCoverUrl
        )
}