package com.leoevg.maftimer.presenter.screens.sections.player


import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.maftimer.presenter.screens.sections.player.components.MusicAssembly

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayer(
    onSpotifyAuthRequest: () -> Unit = {},
    viewModel: MusicPlayerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        if (state.isAuthorized) {
            viewModel.sendEvent(MusicPlayerEvent.OnRefreshPlayback)
        }
    }
    MusicAssembly(state, onEvent = viewModel::sendEvent, onSpotifyAuthRequest)
}



@Preview(showBackground = true)
@Composable
private fun MusicAssemblyPreview() {
    MusicAssembly(
        state = MusicPlayerState(
            isAuthorized = true,
            singer = "Ivo Bobul",
            title = "Balalay",
            isPlaying = true,
            progressMs = 125000L,
            durationMs = 180000L
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}