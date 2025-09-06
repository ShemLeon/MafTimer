package com.leoevg.maftimer.presenter.screens.sections.player.components.ui


import com.leoevg.maftimer.presenter.util.Logx
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
    if (state.isAuthorized) {
        Logx.success(TAG, "Authorized → show player")
        PlayerMain(
            state = state,
            onEvent = onEvent,
            onSpotifyAuthRequest = onSpotifyAuthRequest
        )
    } else {
        Logx.info(TAG, "Showing overlay, isAuthorized=${state.isAuthorized}")
        CustomOverlay(
            onClick = onSpotifyAuthRequest
        )
    }
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