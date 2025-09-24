package com.leoevg.maftimer.presentation.screens.sections.player

import android.Manifest
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.Arrangement
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.leoevg.maftimer.presentation.navigation.NavigationPaths
import com.leoevg.maftimer.presentation.screens.sections.player.components.ui.MusicAssembly
import com.leoevg.maftimer.presentation.screens.sections.timer.components.TypeOfPlayerIndicators
import com.leoevg.maftimer.presentation.util.Logx

private const val TAG = "MusicPlayerComposable"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MusicPlayer(
    navigate: (NavigationPaths) -> Unit,
    viewModel: MusicPlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val musicState by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = { event: MusicPlayerEvent ->
        if (event is MusicPlayerEvent.OnOverlayClicked &&
            musicState.selectedPage == 0 &&
            !musicState.isLocalLoaded
        ) {
            navigate(NavigationPaths.LocalSongsScreenSealed)
        } else {
            Logx.debug(TAG, "Music event: $event")
            viewModel.sendEvent(event)
        }
    }

    Logx.info(TAG, "Music state: isAuth=${musicState.isAuthorizedSpotify}, spotIntent=${musicState.spotIntentActivated}, showOverlay=${musicState.showSpotifyOverlay}, page=${musicState.selectedPage}")
    Logx.debug(TAG, "Music state: $musicState")

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            Logx.debug(TAG, "Periodic check → checking authorization")
            viewModel.refreshRemote()
        }
    }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE,
                Lifecycle.Event.ON_PAUSE,
                Lifecycle.Event.ON_STOP,
                Lifecycle.Event.ON_DESTROY -> {
                    // НЕ показывать оверлеи если авторизован в Spotify
                    if (!musicState.isAuthorizedSpotify || musicState.selectedPage != 1) {
                        viewModel.showAllOverlays()
                    } else {
                        Logx.info(TAG, "🚫 Skipping showAllOverlays - user is authorized in Spotify")
                    }
                }

                Lifecycle.Event.ON_RESUME -> {
                    Logx.info(TAG, "🔄 ON_RESUME: spotIntent=${musicState.spotIntentActivated}, page=${musicState.selectedPage}, showOverlay=${musicState.showSpotifyOverlay}")
                    viewModel.sendEvent(MusicPlayerEvent.OnCheckAuthorization)
                    if (musicState.spotIntentActivated && musicState.selectedPage == 1) {
                        Logx.success(TAG, "✅ Hiding Spotify overlay - conditions met")
                        viewModel.hideAllOverlays()
                    } else {
                        Logx.warn(TAG, "❌ NOT hiding overlay - spotIntent=${musicState.spotIntentActivated}, page=${musicState.selectedPage}")
                    }
                }

                else -> {}     // Handle other lifecycle events
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Детект свайпа и обновление state
    LaunchedEffect(musicState.isAuthorizedSpotify) {
        if (musicState.isAuthorizedSpotify) {
            viewModel.sendEvent(MusicPlayerEvent.OnRefreshPlayback)
        }
    }

// Permission → notify VM once granted (short, Android 13+ only)
    val permissionState = rememberPermissionState(Manifest.permission.READ_MEDIA_AUDIO)
    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            viewModel.onLocalPermissionGranted()
        } else {
            permissionState.launchPermissionRequest()
        }
    }

    val pagerState = rememberPagerState(initialPage = musicState.selectedPage, pageCount = { 2 })  // 0 = local, 1 = spotify
    // Update selectedPage in ViewModel when page changes
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collectLatest { page -> viewModel.updateSelectedPage(page) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        TypeOfPlayerIndicators(selectedPage = pagerState.currentPage)  // Индикаторы сверху
        HorizontalPager(state = pagerState) { page ->
            MusicAssembly(
                state = musicState.copy(selectedPage = page),  // Pass updated state for this page
                onEvent = onEvent,
                onSpotifyAuthRequest = {
                    Logx.action(TAG, "🎵 Spotify auth request - setting spotIntentActivated=true")
                    viewModel.setSpotifyIntentActivated(true)
                    viewModel.spotifyAuthManager.startAuth(context)
                }
            )
        }
    }
}



@Preview(showBackground = true, backgroundColor = 0xFF333333)
@Composable
private fun MusicPlayerSpotifyAuthorizedPreview() {
    MusicPlayer(
        navigate = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF333333)
@Composable
private fun MusicPlayerSpotifyOverlayPreview() {
    MusicPlayer(
        navigate = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF333333)
@Composable
private fun MusicPlayerLocalOverlayPreview() {
    MusicPlayer(
        navigate = {}
    )
}