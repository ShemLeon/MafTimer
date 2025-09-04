package com.leoevg.maftimer.presenter.screens.sections.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.maftimer.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.maftimer.presenter.screens.sections.player.components.ArrowButton
import com.leoevg.maftimer.presenter.screens.sections.player.components.CheckSpotifyAuthorized
import com.leoevg.maftimer.presenter.screens.sections.player.components.CoverAlbumImage
import com.leoevg.maftimer.presenter.screens.sections.player.components.CustomSlider
import com.leoevg.maftimer.presenter.screens.sections.player.components.PlayPauseButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayer(
    onSpotifyAuthRequest: () -> Unit = {},
    viewModel: MusicPlayerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        if (state.isAuthorized) {
            viewModel.sendEvent(MusicPlayerEvent.OnRefreshPlayback)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xCC424242))
            .padding(top = 15.dp, start = 15.dp, end = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
// Информация о песне
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoverAlbumImage(state)
            // Текст исполнитель / название песни
            Column(modifier = Modifier.padding(start = 18.dp)) {
                Text(
                    text = state.singer,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    text = state.title,
                    color = Color.LightGray,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.muzdef),
                contentDescription = "TypePlayerImg",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        if (state.isAuthorized) {
                            viewModel.sendEvent(MusicPlayerEvent.OnRefreshPlayback)
                        } else {
                            onSpotifyAuthRequest()
                        }
                    },
                contentScale = ContentScale.Crop
            )
        }
        // Показываем статус авторизации
        CheckSpotifyAuthorized(state)
        // Слайдер и время
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.width(30.dp),
                text = formatTime(state.progressMs),
                color = Color.LightGray,
                fontSize = 12.sp
            )
            CustomSlider(state, viewModel)
            Text(
                modifier = Modifier.width(30.dp),
                text = formatTime(state.durationMs),
                color = Color.LightGray,
                fontSize = 12.sp
            )
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


private fun formatTime(milliseconds: Long): String {
    val minutes = (milliseconds / 1000) / 60
    val seconds = (milliseconds / 1000) % 60
    return String.format("%d:%02d", minutes, seconds)
}


// TODO - пофиксить превью в плеере.
@Preview(showBackground = true)
@Composable
private fun PlayerContainerPreview() {
    MusicPlayer(
        onSpotifyAuthRequest = {}
    )
}