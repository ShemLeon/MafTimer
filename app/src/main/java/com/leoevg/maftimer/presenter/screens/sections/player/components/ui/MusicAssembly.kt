package com.leoevg.maftimer.presenter.screens.sections.player.components.ui

import TextDurationSong
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerEvent
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState
import androidx.compose.foundation.layout.Box

@Composable
fun MusicAssembly(
    state: MusicPlayerState,
    onEvent: (MusicPlayerEvent) -> Unit,
    onSpotifyAuthRequest: () -> Unit
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xCC424242))
                .padding(top = 15.dp, start = 15.dp, end = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SongInfo(state) // Информация о песне
                Spacer(modifier = Modifier.weight(1f))
                TypePlayerImage(state, onEvent, onSpotifyAuthRequest)
            }
            // Слайдер и время
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextProgressSong(state)
                CustomSlider(state, onEvent)
                TextDurationSong(state)
            }
            // Кнопки управления
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // previous song
                ArrowButton(state = state, onEvent = onEvent)
                Spacer(modifier = Modifier.width(20.dp))
                PlayPauseButton(state, onEvent)
                Spacer(modifier = Modifier.width(20.dp))
                // next song
                ArrowButton(isNext = true, state = state, onEvent = onEvent)
            }
        }

        if (!state.isAuthorized) {
            Log.d("MusicAssembly", "Showing overlay, isAuthorized: ${state.isAuthorized}")
            CustomOverlay(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .clip(RoundedCornerShape(20.dp)),
                onClick = {
                    Log.d("MusicAssembly", "Overlay clicked -> start auth")
                    onSpotifyAuthRequest()
                //    onEvent(MusicPlayerEvent.OnSpotifyAuthRequest)
                }
            )
        } else {
            Log.d("TAG", "Hiding overlay, isAuthorized: ${state.isAuthorized}")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MusicAssemblyPreview() {
    MusicAssembly(
        state = MusicPlayerState(
            isAuthorized = true,
            artist = "Ivo Bobul",
            title = "Balalay",
            isPlaying = true,
            progressMs = 125000L,
            durationMs = 180000L
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MusicAssemblyPreviewOverlay() {
    MusicAssembly(
        state = MusicPlayerState(
            isAuthorized = false,
            artist = "Ivo Bobul",
            title = "Balalay",
            isPlaying = true,
            progressMs = 125000L,
            durationMs = 180000L
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}