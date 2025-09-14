package com.leoevg.maftimer.presenter.screens.sections.player.local

import android.Manifest
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.leoevg.maftimer.data.Song
import com.leoevg.maftimer.data.getSongs
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocalPlayer() {
    val context = LocalContext.current
    val songsState = remember { mutableStateOf<List<Song>>(emptyList()) }
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    var currentIndex by remember { mutableStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var duration by remember { mutableStateOf(0L) }

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val permissionState = rememberPermissionState(permission)

    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            withContext(Dispatchers.IO) {
                songsState.value = getSongs(context)
            }
        }
    }

    LaunchedEffect(currentIndex) {
        val song = songsState.value.getOrNull(currentIndex) ?: return@LaunchedEffect
        exoPlayer.setMediaItem(MediaItem.fromUri(song.data))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) { isPlaying = playing }
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) duration = exoPlayer.duration
                if (state == Player.STATE_ENDED) currentIndex = (currentIndex + 1) % songsState.value.size
            }
        }
        exoPlayer.addListener(listener)
        onDispose { exoPlayer.release() }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            progress = if (duration > 0) exoPlayer.currentPosition.toFloat() / duration else 0f
            delay(500)
        }
    }

    if (!permissionState.status.isGranted) {
        Button(onClick = { permissionState.launchPermissionRequest() }) { Text("Разрешить доступ к файлам") }
    } else if (songsState.value.isEmpty()) {
        Text("Нет песен")
    } else {
        val song = songsState.value[currentIndex]
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), song.albumId),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Text(song.title ?: "Неизвестно", color = Color.White)
            Text(song.artist ?: "Неизвестно", color = Color.White)
            LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
            Row {
                IconButton(onClick = { currentIndex = maxOf(0, currentIndex - 1) }) { Text("Prev") }
                IconButton(onClick = { if (isPlaying) exoPlayer.pause() else exoPlayer.play() }) { Text(if (isPlaying) "Pause" else "Play") }
                IconButton(onClick = { currentIndex = (currentIndex + 1) % songsState.value.size }) { Text("Next") }
            }
        }
    }
}