package com.leoevg.maftimer.presenter.screens.sections.player.components.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.R
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerEvent
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState

@Composable
fun TypePlayerImage(
    state: MusicPlayerState,
    onEvent: (MusicPlayerEvent) -> Unit,
    onSpotifyAuthRequest: () -> Unit
) {
    val onClick = remember(state.isAuthorized) {
        {
            if (state.isAuthorized) {
                onEvent(MusicPlayerEvent.OnRefreshPlayback)
            } else {
                onSpotifyAuthRequest()
            }
        }
    }
    Image(
        painter = painterResource(
            R.drawable.spotify
        ),
        contentDescription = "TypePlayerImg",
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentScale = ContentScale.Crop
    )

}

@Preview(showBackground = true)
@Composable
private fun TypePlayerImagePreview() {
    TypePlayerImage(
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