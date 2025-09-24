package com.leoevg.maftimer.presentation.screens.sections.player.local

import com.leoevg.maftimer.data.Song

data class LocalPlayback(
    val isReady: Boolean = false,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val currentIndex: Int = 0,
    val currentSong: Song? = null
)
