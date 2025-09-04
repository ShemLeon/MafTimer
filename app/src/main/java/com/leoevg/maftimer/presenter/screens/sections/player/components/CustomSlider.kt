package com.leoevg.maftimer.presenter.screens.sections.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerEvent
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RowScope.CustomSlider(
    state: MusicPlayerState,
    viewModel: MusicPlayerViewModel
) {
    Slider(
        value = if (state.durationMs > 0)
            state.progressMs.toFloat() / state.durationMs.toFloat()
        else 0f,
        onValueChange = { newValue ->
            if (state.isAuthorized) {
                val newPosition = (newValue * state.durationMs).toLong()
                viewModel.sendEvent(MusicPlayerEvent.OnSeekTo(newPosition))
            }
        },
        modifier = Modifier
            .weight(1f)
            .height(20.dp),
        enabled = state.isAuthorized,
        colors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = Color.White,
            inactiveTrackColor = Color.Gray.copy(alpha = 0.5f)
        ),
        thumb = {
            Box(
                modifier = Modifier
                    .size(13.dp)
                    .background(Color.White, CircleShape)
            )
        },
        track = {
            SliderDefaults.Track(
                modifier = Modifier.height(8.dp),
                sliderState = it,
                colors = SliderDefaults.colors(
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.Gray.copy(alpha = 0.5f)
                ),
                enabled = state.isAuthorized
            )
        }
    )
}