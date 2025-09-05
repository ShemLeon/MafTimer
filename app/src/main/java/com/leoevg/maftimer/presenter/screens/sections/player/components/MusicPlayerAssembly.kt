package com.leoevg.maftimer.presenter.screens.sections.player.components

import TextDurationSong
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
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerViewModel
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.ArrowButton
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.CheckSpotifyAuthorized
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.CustomSlider
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.PlayPauseButton
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.SongInfo
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.TextProgressSong
import com.leoevg.maftimer.presenter.screens.sections.player.components.ui.TypePlayerImage

@Composable
fun MusicPlayerContent(
    state: MusicPlayerState,
    viewModel: MusicPlayerViewModel,
    onSpotifyAuthRequest: () -> Unit
) {
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
            TypePlayerImage(state, viewModel, onSpotifyAuthRequest)
        }
        // Показываем статус авторизации
        CheckSpotifyAuthorized(state)
        // Слайдер и время
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextProgressSong(state)
            CustomSlider(state, viewModel)
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
            ArrowButton(state = state, viewModel = viewModel)
            Spacer(modifier = Modifier.width(20.dp))
            PlayPauseButton(state, viewModel)
            Spacer(modifier = Modifier.width(20.dp))
            // next song
            ArrowButton(isNext = true, state = state, viewModel = viewModel)
        }
    }
}