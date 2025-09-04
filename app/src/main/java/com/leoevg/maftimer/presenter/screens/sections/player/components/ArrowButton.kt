package com.leoevg.maftimer.presenter.screens.sections.player.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
    Image(
        painter = painterResource(R.drawable.btn_strelki),
        contentDescription = if (isNext) "Next song" else "Previous song",
        modifier = Modifier
            .rotate(
                if (isNext) 0f
                else 180f
            )
            .size(40.dp)
            .clickable {
                if (state.isAuthorized) {
                    viewModel.sendEvent(
                        if (isNext) MusicPlayerEvent.OnNextSongBtnClicked
                        else MusicPlayerEvent.OnPreviousSongBtnClicked
                    )
                }
            },
        contentScale = ContentScale.Fit
    )
}