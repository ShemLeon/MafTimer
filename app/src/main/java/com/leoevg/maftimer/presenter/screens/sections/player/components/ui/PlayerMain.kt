package com.leoevg.maftimer.presenter.screens.sections.player.components.ui


import androidx.compose.foundation.layout.height
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.info.TextDurationSong
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerEvent
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState
import androidx.compose.material3.Surface
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.buttons.NextSongButton
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.buttons.PlayPauseButton
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.buttons.PrevSongButton
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.info.CustomSlider
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.info.SongInfo
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.info.TextProgressSong
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.info.TypePlayerImage

@Composable
fun PlayerMain(
    state: MusicPlayerState,
    onEvent: (MusicPlayerEvent) -> Unit,
    onSpotifyAuthRequest: () -> Unit
) {
    Surface(
        color = Color(0xCC424242),  // ваш оригинальный цвет
        tonalElevation = 2.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 25.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 15.dp, end = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SongInfo(state)
                }
                TypePlayerImage(state, onEvent, onSpotifyAuthRequest)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextProgressSong(state)
                CustomSlider(state, onEvent)
                TextDurationSong(state)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PrevSongButton(onEvent = onEvent)
                Spacer(modifier = Modifier.width(20.dp))
                PlayPauseButton(state, onEvent)
                Spacer(modifier = Modifier.width(20.dp))
                NextSongButton(onEvent = onEvent)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayerMainPreview() {
    PlayerMain(
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
private fun PlayerMainLongPreview() {
    PlayerMain(
        state = MusicPlayerState(
            isAuthorized = true,
            artist = "ой у вишневому саду там соловейко",
            title = "Balalay",
            isPlaying = false,
            progressMs = 125000L,
            durationMs = 180000L
        ),
        onEvent = {},
        onSpotifyAuthRequest = {}
    )
}