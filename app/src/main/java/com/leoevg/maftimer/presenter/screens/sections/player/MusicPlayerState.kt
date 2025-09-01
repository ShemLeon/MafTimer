package com.leoevg.maftimer.presenter.screens.sections.player

data class MusicPlayerState (
    val singer: String = "singer",
    val title: String = "title",
    val isPlaying: Boolean = true,
    val progressMs: Long = 50.toLong(),
    val durationMs: Long = 160.toLong()
)