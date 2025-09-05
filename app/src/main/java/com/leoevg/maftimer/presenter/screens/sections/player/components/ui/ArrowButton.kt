package com.leoevg.maftimer.presenter.screens.sections.player.components.ui

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
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerViewModel

@Composable
fun ArrowButton(
    state: MusicPlayerState,
    viewModel: MusicPlayerViewModel,
    isNext: Boolean = false
) {
    ArrowButtonContent(
        isNext = isNext,
        isAuthorized = state.isAuthorized,
        onNextClick = {
            viewModel.sendEvent(MusicPlayerEvent.OnNextSongBtnClicked)
        },
        onPreviousClick = {
            viewModel.sendEvent(MusicPlayerEvent.OnPreviousSongBtnClicked)
        }
    )
}

@Composable
fun ArrowButtonContent(
    isNext: Boolean = false,
    isAuthorized: Boolean = true,
    onNextClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {}
) {
    Icon(
        painter = painterResource(R.drawable.btn_strelki),
        contentDescription = if (isNext) "Next song" else "Previous song",
        modifier = Modifier
            .rotate(
                if (isNext) 0f
                else 180f
            )
            .size(40.dp)
            .clickable {
                if (isAuthorized) {
                    if (isNext) MusicPlayerEvent.OnNextSongBtnClicked
                    else MusicPlayerEvent.OnPreviousSongBtnClicked
                }
            },
        tint = Color(0x80fc520d)
    )
}


@Preview(showBackground = true, name = "Previous Button")
@Composable
private fun ArrowButtonContentPreview() {
    Surface(
        color = Color.Black,
        modifier = Modifier.padding(16.dp)
    ) {
        ArrowButtonContent(
            isNext = false,
            isAuthorized = true
        )
    }
}

