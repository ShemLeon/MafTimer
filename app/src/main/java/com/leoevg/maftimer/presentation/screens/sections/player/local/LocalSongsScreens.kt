package com.leoevg.maftimer.presentation.screens.sections.player.local

import android.Manifest

import android.content.ContentUris
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.leoevg.maftimer.data.Song
import com.leoevg.maftimer.data.getSongs

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LocalSongsScreen(
    onBack: () -> Unit,
    onPick: (List<Song>, Int) -> Unit
) {
    val ctx = LocalContext.current
    val permission = Manifest.permission.READ_MEDIA_AUDIO
    val permissionState = rememberPermissionState(permission)
    var songs by remember { mutableStateOf<List<Song>>(emptyList()) }

    LaunchedEffect(permissionState.status) {
        if (permissionState.status.isGranted) {
            songs = getSongs(ctx)
        } else {
            permissionState.launchPermissionRequest()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Local songs") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            itemsIndexed(songs) { index, song ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPick(songs, index) }
                        .padding(12.dp)
                ) {
                    AsyncImage(
                        model = ContentUris.withAppendedId(
                            Uri.parse("content://media/external/audio/albumart"),
                            song.albumId
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.fillMaxWidth()) {
                        Text(song.title ?: "Unknown", style = MaterialTheme.typography.titleMedium)
                        Text(song.artist ?: "", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Divider()
            }
        }
    }
}