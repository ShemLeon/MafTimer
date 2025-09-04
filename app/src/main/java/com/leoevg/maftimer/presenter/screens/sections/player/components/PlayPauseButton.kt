package com.leoevg.maftimer.presenter.screens.sections.player.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.R
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerEvent
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerViewModel

@Composable
fun PlayPauseButton(
    state: MusicPlayerState,
    viewModel: MusicPlayerViewModel
) {
    Image(
        painter = painterResource(
            if (state.isPlaying)
                R.drawable.btn_pause
            else
                R.drawable.btn_start
        ),
        contentDescription = if (state.isPlaying) "pause" else "play",
        modifier = Modifier
            .size((40 * 1.1).dp)
            .clickable {
                if (state.isAuthorized) {
                    if (state.isPlaying)
                        viewModel.sendEvent(MusicPlayerEvent.OnPauseBtnClicked)
                    else viewModel.sendEvent(MusicPlayerEvent.OnStartBtnClicked)
                }
            },
        contentScale = ContentScale.Fit
    )
}