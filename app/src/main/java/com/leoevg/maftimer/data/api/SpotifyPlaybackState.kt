package com.leoevg.maftimer.data.api

data class SpotifyPlaybackState(
    val isPlaying: Boolean,
    val progressMs: Long?,
    val item: SpotifyTrack?
)