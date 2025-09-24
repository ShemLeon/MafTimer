package com.leoevg.maftimer.presentation.screens.sections.player.spotify

data class RemotePlayback(
    val isPlaying: Boolean = false,
    val progressMs: Long = 0L,
    val durationMs: Long = 0L,
    val title: String = "",
    val artist: String = "",
    val albumCoverUrl: String = ""
)
