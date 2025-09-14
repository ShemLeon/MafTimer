package com.leoevg.maftimer.presenter.screens.sections.player.components.ui.buttons

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Icon

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.R
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerEvent


@Composable
fun NextSongButton(
    onEvent: (MusicPlayerEvent) -> Unit
) {
    IconButton(onClick = {
        onEvent(MusicPlayerEvent.OnNextSongBtnClicked)
    }){
        Icon(
            painter = painterResource(R.drawable.outline_skip_next_24),
            contentDescription = "Next song",
            modifier = Modifier
                .size(40.dp),
            tint = Color(0x80fc520d)
        )
    }

}


@Preview(showBackground = true, name = "NextSong Button")
@Composable
private fun NextSongButtonPreview() {
    Surface(
        color = Color.Black,
        modifier = Modifier.padding(16.dp)
    ) {
        NextSongButton(
            onEvent = { }
        )
    }
}

