package com.leoevg.maftimer.presentation.screens.sections.player

data class MusicPlayerState (
    val isAuthorizedSpotify: Boolean = false,
    val isPlaying: Boolean = true,
    val selectedPage: Int = 1,           // 0 = Local, 1 = spotify
    val isLocalLoaded: Boolean = false,  // false -> show local overlay, true -> show local overlay
    val showSpotifyOverlay: Boolean = true, // управляет видимостью Spotify оверлея
    val showLocalOverlay: Boolean = true, // управляет видимостью Spotify оверлея
    val spotIntentActivated: Boolean = false, // отслеживает запуск Spotify через интент

    val artist: String = "singer",
    val title: String = "title",
    val progressMs: Long = 50L,
    val durationMs: Long = 160L,
    val albumCoverUrl: String? = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)
