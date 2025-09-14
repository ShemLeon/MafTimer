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
    if (state.selectedPage == 0 || state.isAuthorized) {
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
            selectedPage = 0  // Локальный оверлей
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}



@Preview(showBackground = true)
@Composable
private fun MusicAssemblyPreview() {
    MusicAssembly(
        state = MusicPlayerState(
            isAuthorized = true,
            artist = "Ivo Bobul",
            title = "Balalay",
            isPlaying = true,
            progressMs = 125000L,
            durationMs = 180000L
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MusicAssemblyPreviewOverlay() {
    MusicAssembly(
        state = MusicPlayerState(
            isAuthorized = false,
            artist = "Ivo Bobul",
            title = "Balalay",
            isPlaying = true,
            progressMs = 125000L,
            durationMs = 180000L
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}