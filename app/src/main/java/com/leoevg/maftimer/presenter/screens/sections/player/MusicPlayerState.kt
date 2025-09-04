package com.leoevg.maftimer.presenter.screens.sections.player

data class MusicPlayerState (
    val isAuthorized: Boolean = false,
    val singer: String = "singer",
    val title: String = "title",
    val isPlaying: Boolean = true,
    val progressMs: Long = 50L,
    val durationMs: Long = 160L,
    val isLoading: Boolean = false,
    val error: String? = null
)