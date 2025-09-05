package com.leoevg.maftimer.presenter.screens.sections.player.components.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.R
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState

@Composable
fun CoverAlbumImage(state: MusicPlayerState) {
    Image(
        painter = painterResource(
            if (!state.isAuthorized)
                R.drawable.coverdef
            else R.drawable.muzdef
            // TODO: настроить после активации плеера
        ),
        contentDescription = "album cover",
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
}