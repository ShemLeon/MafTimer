package com.leoevg.maftimer.presenter.screens.sections.player.components.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.R

@Composable
fun CustomOverlay(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    state: MusicPlayerState
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(horizontal = 25.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF000000))
            .clickable { onClick() }
            .padding(top = 15.dp, start = 15.dp, end = 15.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(
                if (state.selectedPage == 0) R.drawable.own_overlay
                else if (state.selectedPage == 1) R.drawable.spotify_overlay
                else R.drawable.spotify_overlay
            ),
            contentDescription = "Open Spotify",
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 160)
@Composable
fun SpotifyOverlayPreview() {
    CustomOverlay(
        state = MusicPlayerState(selectedPage = 1),
        modifier = Modifier.size(96.dp),
        onClick = {}
    )
}

@Preview(showBackground = true, widthDp = 411, heightDp = 160)
@Composable
fun OwnOverlayPreview() {
    CustomOverlay(
        state = MusicPlayerState(selectedPage = 0),
        modifier = Modifier.size(96.dp),
        onClick = {}
    )
}


