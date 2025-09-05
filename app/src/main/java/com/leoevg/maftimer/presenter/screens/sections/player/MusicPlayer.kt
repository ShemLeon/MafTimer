package com.leoevg.maftimer.presenter.screens.sections.player

import TextDurationSong
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.width
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.maftimer.presenter.screens.sections.player.components.MusicPlayerContent
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.ArrowButton
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.CheckSpotifyAuthorized
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.CustomSlider
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.PlayPauseButton
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.SongInfo
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.TextProgressSong
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.TypePlayerImage

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
    MusicPlayerContent(state, viewModel, onSpotifyAuthRequest)
}



// TODO - пофиксить превью в плеере.
@Preview(showBackground = true)
@Composable
private fun PlayerContainerPreview() {
    MusicPlayer(
        onSpotifyAuthRequest = {}
    )
}