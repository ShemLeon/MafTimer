package com.leoevg.maftimer.presenter.screens.sections.player


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.MusicAssembly

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayer(
    onSpotifyAuthRequest: () -> Unit = {},
    state: MusicPlayerState? = null,
    onEvent: ((MusicPlayerEvent) -> Unit)? = null,
    viewModel: MusicPlayerViewModel? = if (state == null) hiltViewModel() else null,
) {
    val actualState = state ?: viewModel?.state?.collectAsState()?.value ?: MusicPlayerState()
    val actualOnEvent = onEvent ?: viewModel?.let { { event -> it.sendEvent(event) } } ?: {}

    LaunchedEffect(Unit) {
        if (actualState.isAuthorized && viewModel != null) {
            viewModel.sendEvent(MusicPlayerEvent.OnRefreshPlayback)
        }
    }
    MusicAssembly(actualState, onEvent = actualOnEvent, onSpotifyAuthRequest)
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
private fun MusicAssemblyOverlayPreview() {
    MusicAssembly(
        state = MusicPlayerState(
            isAuthorized = false,
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}