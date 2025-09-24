package com.leoevg.maftimer.presentation.screens.sections.player.components.ui.info

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.maftimer.presentation.screens.sections.player.MusicPlayerState
import com.leoevg.maftimer.presentation.util.formatTime


@Composable
fun TextDurationSong(state: MusicPlayerState) {
    Text(
        modifier = Modifier.width(35.dp),
        text = state.durationMs.formatTime(),
        color = Color(0xE1D1CFCF),
        fontSize = 12.sp
    )
}

@Preview(showBackground = true, name = "TextDurationSong")
@Composable
fun TextDurationSongPreview(){
    val mockState = MusicPlayerState(
        durationMs = 600000L
    )
    TextDurationSong(mockState)
}
