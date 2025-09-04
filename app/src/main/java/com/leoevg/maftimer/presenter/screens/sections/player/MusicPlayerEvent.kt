package com.leoevg.maftimer.presenter.screens.sections.player

sealed interface MusicPlayerEvent {
    object OnSpotifyAuthRequest: MusicPlayerEvent
    object OnStartBtnClicked: MusicPlayerEvent
    object OnPauseBtnClicked: MusicPlayerEvent
    object OnNextSongBtnClicked: MusicPlayerEvent
    object OnPreviousSongBtnClicked: MusicPlayerEvent
    data class OnSeekTo(val positionMs: Long): MusicPlayerEvent
    object OnRefreshPlayback: MusicPlayerEvent
}