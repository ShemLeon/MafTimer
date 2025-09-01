package com.leoevg.maftimer.presenter.screens.sections.player

data class MusicPlayerState (
    val singer: String,
    val title: String,
    val isPlaying: Boolean,
    val progressMs: Long,
    val durationMs: Long
    // Todo: значения по умолчанию =чтото
)