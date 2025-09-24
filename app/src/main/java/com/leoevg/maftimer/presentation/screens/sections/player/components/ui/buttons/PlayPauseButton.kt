package com.leoevg.maftimer.presentation.screens.sections.player.components.ui.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.R
import com.leoevg.maftimer.presentation.screens.sections.player.MusicPlayerEvent
import com.leoevg.maftimer.presentation.screens.sections.player.MusicPlayerState

@Composable
fun PlayPauseButton(
    state: MusicPlayerState,
    onEvent: (MusicPlayerEvent) -> Unit
) {
    var localIsPlaying by remember { mutableStateOf(state.isPlaying) }

    // Sync with external state
    LaunchedEffect(state.isPlaying) {
        localIsPlaying = state.isPlaying
    }

    Image(
        painter = painterResource(
            if (localIsPlaying)
                R.drawable.btn_pause
            else
                R.drawable.btn_start
        ),
        contentDescription = if (localIsPlaying) "pause" else "play",
        modifier = Modifier
            .size((40 * 1.1).dp)
            .clickable {
                // Immediately toggle UI state
                localIsPlaying = !localIsPlaying

                // Send the appropriate event
                if (state.isPlaying) onEvent(MusicPlayerEvent.OnPauseBtnClicked)
                else onEvent(MusicPlayerEvent.OnStartBtnClicked)
            },
        contentScale = ContentScale.Fit
    )
}