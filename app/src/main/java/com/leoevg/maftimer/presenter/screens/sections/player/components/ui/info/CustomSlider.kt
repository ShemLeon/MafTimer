package com.leoevg.maftimer.presenter.screens.sections.player.components.ui.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerEvent
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RowScope.CustomSlider(
    state: MusicPlayerState,
    onEvent: (MusicPlayerEvent) -> Unit
) {
    Slider(
        value = if (state.durationMs > 0)
            state.progressMs.toFloat() / state.durationMs.toFloat()
        else 0f,
        onValueChange = { newValue ->
            if (state.isAuthorized) {
                val newPosition = (newValue * state.durationMs).toLong()
                onEvent(MusicPlayerEvent.OnSeekTo(newPosition))
            }
        },
        modifier = Modifier
            .weight(1f)
            .height(15.dp),
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
                    .background(
                        color = Color(0x80FFFFFF),
                        CircleShape
                    )
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

@Preview(showBackground = true)
@Composable
private fun CustomSliderPreview() {
    Row {
        CustomSlider(
            state = MusicPlayerState(
                isAuthorized = true,
                artist = "Ivo Bobul",
                title = "Balalay",
                isPlaying = true,
                progressMs = 125000L,
                durationMs = 180000L
            ),
            onEvent = {}
        )
    }
}