package com.leoevg.maftimer.presenter.screens.sections.player.components.ui.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState

@Composable
fun SongInfo(state: MusicPlayerState) {
    CoverAlbumImage(state)
    // Текст исполнитель / название песни
    Column(modifier = Modifier.padding(start = 18.dp)) {
        Text(
            text = state.artist,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Text(
            text = state.title,
            color = Color.LightGray,
            fontSize = 16.sp
        )
    }
}