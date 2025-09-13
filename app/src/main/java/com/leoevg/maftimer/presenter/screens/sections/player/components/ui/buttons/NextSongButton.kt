package com.leoevg.maftimer.presenter.screens.sections.player.components.ui.buttons

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.R
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerEvent
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState

@Composable
fun NextSongButton(
    state: MusicPlayerState,
    onEvent: (MusicPlayerEvent) -> Unit,
    isNext: Boolean = false
) {
    NextSongButtonContent(
        isNext = isNext,
        isAuthorized = state.isAuthorized,
        onNextClick = {
            onEvent(MusicPlayerEvent.OnNextSongBtnClicked)
        }
    )
}

@Composable
fun NextSongButtonContent(
    isNext: Boolean = false,
    isAuthorized: Boolean = true,
    onNextClick: () -> Unit = {}
) {
    Icon(
        painter = painterResource(R.drawable.outline_skip_next_24),
        contentDescription = "Next song",
        modifier = Modifier
            .size(40.dp)
            .clickable {
                if (isAuthorized) {
                   onNextClick()
                }
            },
        tint = Color(0x80fc520d)
    )
}


@Preview(showBackground = true, name = "NextSong Button")
@Composable
private fun NextSongButtonContentPreview() {
    Surface(
        color = Color.Black,
        modifier = Modifier.padding(16.dp)
    ) {
        NextSongButtonContent(
            isNext = false,
            isAuthorized = true
        )
    }
}

