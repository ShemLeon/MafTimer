package com.leoevg.maftimer.presenter.screens.sections.player.components.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.R

@Composable
fun CustomOverlay(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(Color(0xFF000000))
            .clickable { onClick() }
        ,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.spotify_overlay),
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
    CustomOverlay(modifier = Modifier.size(96.dp), onClick = {})
}
