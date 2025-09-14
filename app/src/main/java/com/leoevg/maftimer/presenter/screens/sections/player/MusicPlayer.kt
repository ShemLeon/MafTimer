package com.leoevg.maftimer.presenter.screens.sections.player

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.leoevg.maftimer.presenter.screens.sections.player.components.TypeOfPlayerIndicators
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.MusicAssembly
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

    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 3 })  // 3 страницы: 0 - dummy left, 1 - main UI, 2 - dummy right


    val coroutineScope = rememberCoroutineScope()

    // Детект свайпа и обновление state
    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage != 1) {
            val newSelectedPage = if (pagerState.currentPage == 0) 0 else 1  // Left swipe -> local (0), right -> spotify (1)
            viewModel?.updateSelectedPage(newSelectedPage)  // Обновляем state в ViewModel
            coroutineScope.launch {
                pagerState.animateScrollToPage(1)  // Возврат на main UI
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        TypeOfPlayerIndicators(selectedPage = pagerState.currentPage)  // Индикаторы сверху
        // Добавили Spacer для расстояния между индикаторами и pager (чтобы не сливались)

        HorizontalPager(state = pagerState) { page ->
            if (page == 1) {
                MusicAssembly(
                    state = actualState,  // Единый UI, данные/оверлей зависят от actualState.selectedPage
                    onEvent = actualOnEvent,
                    onSpotifyAuthRequest = onSpotifyAuthRequest
                )
            } else {
                // Dummy для свайпа (пустой, чтобы не показывать ничего лишнего)
                Box(modifier = Modifier.fillMaxSize()) {}
            }
        }
    }
}


// 1. Preview: Spotify OK, inside with buttons (authorized, selectedPage = 1)
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

// 2. Preview: Spotify overlay (not authorized, selectedPage = 1)
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

// 3. Preview: Local overlay (selectedPage = 0, assume not "loaded" for overlay)
@Preview(showBackground = true)
@Composable
private fun MusicPlayerLocalOverlayPreview() {
    MusicPlayer(
        state = MusicPlayerState(
            isAuthorized = false,
            selectedPage = 0
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}