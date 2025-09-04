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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerContainer(
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
            Image(
                painter = painterResource(R.drawable.ernest),
                contentDescription = "Обложка альбома",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(start = 18.dp)) {
                Text(
                    text = state.singer,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(text = state.title, color = Color.LightGray, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.spotify),
                contentDescription = "spotify",
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

            Slider(
                value = if (state.durationMs > 0) state.progressMs.toFloat() / state.durationMs.toFloat() else 0f,
                onValueChange = { newValue ->
                    if (isAuthorized) {
                        val newPosition = (newValue * state.durationMs).toLong()
//                        viewModel.seekTo(newPosition)
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(20.dp),
                enabled = isAuthorized,
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
                        enabled = isAuthorized
                    )
                }
            )

            Text(
                modifier = Modifier.width(30.dp),
                text = formatTime(state.durationMs),
                color = Color.LightGray,
                fontSize = 12.sp
            )
        }

        // Кнопки управления
        val buttonSize = 40
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.btn_strelki),
                contentDescription = "previous",
                modifier = Modifier
                    .rotate(180f)
                    .size(buttonSize.dp)
                    .clickable {
//                        if (isAuthorized) viewModel.previous()
                    },
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(20.dp))

            Image(
                painter = painterResource(R.drawable.btn_pause),
                contentDescription = if (state.isPlaying) "pause" else "play",
                modifier = Modifier
                    .size((buttonSize * 1.1).dp)
                    .clickable {
                        if (isAuthorized) {
//                            if (state.isPlaying) viewModel.pause() else viewModel.play()
                        }
                    },
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(20.dp))

            Image(
                painter = painterResource(R.drawable.btn_strelki),
                contentDescription = "next",
                modifier = Modifier
                    .size(buttonSize.dp)
                    .clickable {
//                        if (isAuthorized) viewModel.next()
                    },
                contentScale = ContentScale.Fit
            )
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
    PlayerContainer(
        onSpotifyAuthRequest = {}
    )
}