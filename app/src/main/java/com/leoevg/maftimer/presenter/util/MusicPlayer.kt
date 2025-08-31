package com.leoevg.maftimer.presenter.util

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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoevg.maftimer.data.api.SpotifyPlaybackState
import com.leoevg.maftimer.data.repository.SpotifyRepository
import com.leoevg.maftimer.util.SpotifyAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val spotifyRepository: SpotifyRepository,
    private val authManager: SpotifyAuthManager
) : ViewModel() {

    val playbackState: StateFlow<SpotifyPlaybackState?> = spotifyRepository.playbackState

    init {
        // Устанавливаем токен при инициализации
        authManager.getStoredToken()?.let { token ->
            spotifyRepository.setAccessToken(token)
        }
    }

    fun play() {
        viewModelScope.launch {
            spotifyRepository.play()
        }
    }

    fun pause() {
        viewModelScope.launch {
            spotifyRepository.pause()
        }
    }

    fun next() {
        viewModelScope.launch {
            spotifyRepository.next()
        }
    }

    fun previous() {
        viewModelScope.launch {
            spotifyRepository.previous()
        }
    }

    fun seekTo(positionMs: Long) {
        viewModelScope.launch {
            spotifyRepository.seekTo(positionMs)
        }
    }

    fun refreshPlayback() {
        viewModelScope.launch {
            spotifyRepository.getCurrentPlayback()
        }
    }

    fun isAuthorized(): Boolean {
        return authManager.getStoredToken() != null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerContainer(
    viewModel: MusicPlayerViewModel = hiltViewModel(),
    onSpotifyAuthRequest: () -> Unit = {}
) {
    val playbackState by viewModel.playbackState.collectAsState()
    val isAuthorized = viewModel.isAuthorized()

    LaunchedEffect(Unit) {
        if (isAuthorized) {
            viewModel.refreshPlayback()
        }
    }

    val singer = playbackState?.item?.artists?.firstOrNull()?.name ?: "Maser"
    val title = playbackState?.item?.name ?: "На магистрейте"
    val isPlaying = playbackState?.is_playing ?: false
    val progressMs = playbackState?.progress_ms ?: 0L
    val durationMs = playbackState?.item?.duration_ms ?: 90000L

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
                    text = singer,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(text = title, color = Color.LightGray, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.spotify),
                contentDescription = "spotify",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        if (isAuthorized) {
                            viewModel.refreshPlayback()
                        } else {
                            onSpotifyAuthRequest()
                        }
                    },
                contentScale = ContentScale.Crop
            )
        }

        // Показываем статус авторизации
        if (!isAuthorized) {
            Text(
                text = "Нажмите на Spotify для авторизации",
                color = Color.Yellow,
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Слайдер и время
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.width(30.dp),
                text = formatTime(progressMs),
                color = Color.LightGray,
                fontSize = 12.sp
            )

            Slider(
                value = if (durationMs > 0) progressMs.toFloat() / durationMs.toFloat() else 0f,
                onValueChange = { newValue ->
                    if (isAuthorized) {
                        val newPosition = (newValue * durationMs).toLong()
                        viewModel.seekTo(newPosition)
                    }
                },
                modifier = Modifier.weight(1f).height(20.dp),
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
                text = formatTime(durationMs),
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
                        if (isAuthorized) viewModel.previous()
                    },
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(20.dp))

            Image(
                painter = painterResource(R.drawable.btn_pause),
                contentDescription = if (isPlaying) "pause" else "play",
                modifier = Modifier
                    .size((buttonSize * 1.1).dp)
                    .clickable {
                        if (isAuthorized) {
                            if (isPlaying) viewModel.pause() else viewModel.play()
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
                        if (isAuthorized) viewModel.next()
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