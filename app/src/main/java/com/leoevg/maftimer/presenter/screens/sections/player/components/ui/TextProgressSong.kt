package com.leoevg.maftimer.presenter.screens.sections.player.components.ui

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState
import com.leoevg.maftimer.presenter.util.formatTime

@Composable
fun TextProgressSong(state: MusicPlayerState) {
    Text(
        modifier = Modifier.width(30.dp),
        text = state.progressMs.formatTime(),
        color = Color.LightGray,
        fontSize = 12.sp
    )
}

@Preview(showBackground = true, name = "TextProgressSong")
@Composable
fun TextProgressSongPreview(){
    val mockState = MusicPlayerState(
        progressMs = 125000L
    )
    TextProgressSong(mockState)
}