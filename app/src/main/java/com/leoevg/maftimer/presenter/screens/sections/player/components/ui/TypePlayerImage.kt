package com.leoevg.maftimer.presenter.screens.sections.player.components.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.R
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerEvent
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerViewModel

@Composable
fun TypePlayerImage(
    state: MusicPlayerState,
    onEvent: (MusicPlayerEvent) -> Unit,
    onSpotifyAuthRequest: () -> Unit
) {
    Image(
        painter = painterResource(
            R.drawable.muzdef
        ),
        contentDescription = "TypePlayerImg",
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                if (state.isAuthorized) {
                    onEvent(MusicPlayerEvent.OnRefreshPlayback)
                } else {
                    onSpotifyAuthRequest()
                }
            },
        contentScale = ContentScale.Crop
    )
}