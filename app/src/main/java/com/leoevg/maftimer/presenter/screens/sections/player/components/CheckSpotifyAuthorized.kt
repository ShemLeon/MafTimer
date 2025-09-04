package com.leoevg.maftimer.presenter.screens.sections.player.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState

@Composable
fun CheckSpotifyAuthorized(state: MusicPlayerState) {
    if (!state.isAuthorized) {
        Text(
            text = "Нажмите на Spotify для авторизации",
            color = Color.Yellow,
            fontSize = 12.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}