package com.leoevg.maftimer.presenter.screens.sections.player

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.Arrangement
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.MusicAssembly
import com.leoevg.maftimer.presenter.screens.sections.timer.components.TypeOfPlayerIndicators
import kotlinx.coroutines.launch
// Временно убрал импорт LocalPlayer
// import com.leoevg.maftimer.presenter.screens.sections.player.local.LocalPlayer


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayer(
    onSpotifyAuthRequest: () -> Unit = {},
    state: MusicPlayerState? = null,
    onEvent: ((MusicPlayerEvent) -> Unit)? = null,
    viewModel: MusicPlayerViewModel? = if (state == null) hiltViewModel() else null
) {
    val actualState = state ?: viewModel?.state?.collectAsState()?.value ?: MusicPlayerState()
    val actualOnEvent = onEvent ?: viewModel?.let { { event -> it.sendEvent(event) } } ?: {}

    // Детект свайпа и обновление state
    LaunchedEffect(Unit) {
        if (actualState.isAuthorized && viewModel != null) {
            viewModel.sendEvent(MusicPlayerEvent.OnRefreshPlayback)
        }
    }

    val pagerState = rememberPagerState(initialPage = actualState.selectedPage, pageCount = { 2 })  // 0 = local, 1 = spotify
    // Update selectedPage in ViewModel when page changes
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collectLatest { page -> viewModel?.updateSelectedPage(page) }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        TypeOfPlayerIndicators(selectedPage = pagerState.currentPage)  // Индикаторы сверху
        HorizontalPager(state = pagerState) { page ->
            MusicAssembly(
                state = actualState.copy(selectedPage = page),  // Pass updated state for this page
                onEvent = actualOnEvent,
                onSpotifyAuthRequest = onSpotifyAuthRequest
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun MusicPlayerSpotifyAuthorizedPreview() {
    MusicPlayer(
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
private fun MusicPlayerSpotifyOverlayPreview() {
    MusicPlayer(
        state = MusicPlayerState(
            isAuthorized = false,
            selectedPage = 1
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MusicPlayerLocalOverlayPreview() {
    MusicPlayer(
        state = MusicPlayerState(
            isLocalLoaded = false,  // Not loaded - trigger local overlay
            selectedPage = 0
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}