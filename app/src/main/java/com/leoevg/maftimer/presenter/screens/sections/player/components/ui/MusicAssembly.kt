package com.leoevg.maftimer.presenter.screens.sections.player.components.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerEvent
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState

private const val TAG = "MusicAssembly"

@Composable
fun MusicAssembly(
    state: MusicPlayerState,
    onEvent: (MusicPlayerEvent) -> Unit,
    onSpotifyAuthRequest: () -> Unit
) {
    val isLocal = state.selectedPage == 0
    // Логика: для local - if isLocalLoaded, for spotify - if isAuthorized
    if  ((isLocal && state.isLocalLoaded) || (!isLocal && state.isAuthorized)) {
        PlayerMain(state = state, onEvent = onEvent, onSpotifyAuthRequest = onSpotifyAuthRequest)
    } else {
        CustomOverlay(onClick = onSpotifyAuthRequest, state = state)
    }
}
@Preview(showBackground = true)
@Composable
private fun MusicAssemblyLocalOverlayPreview() {
    MusicAssembly(
        state = MusicPlayerState(
            isAuthorized = false,
            isLocalLoaded = false,  // Not loaded - show local overlay
            selectedPage = 0
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MusicAssemblySpotifyOverlayPreview() {
    MusicAssembly(
        state = MusicPlayerState(
            isAuthorized = false,
            selectedPage = 1  // Not authorized - show spotify overlay
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MusicAssemblyAuthorizedPreview() {
    MusicAssembly(
        state = MusicPlayerState(
            isAuthorized = true,
            artist = "Ivo Bobul",
            title = "Balalay",
            isPlaying = true,
            progressMs = 125000L,
            durationMs = 180000L,
            selectedPage = 1
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MusicAssemblyLocalLoadedPreview() {
    MusicAssembly(
        state = MusicPlayerState(
            isLocalLoaded = true,  // Loaded - show PlayerMain for local
            artist = "Local Artist",
            title = "Local Song",
            isPlaying = true,
            progressMs = 125000L,
            durationMs = 180000L,
            selectedPage = 0
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}