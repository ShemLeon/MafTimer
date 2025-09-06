package com.leoevg.maftimer.presenter.screens.sections.player.components.ui


import android.util.Log
import com.leoevg.maftimer.presenter.util.Logx
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerEvent
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.buttons.ArrowButton
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.buttons.PlayPauseButton
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.info.CustomSlider
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.info.SongInfo
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.info.TextProgressSong
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.info.TypePlayerImage
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
            onClick = { onEvent(MusicPlayerEvent.OnOverlayClicked) }
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