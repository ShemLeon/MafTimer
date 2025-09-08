package com.leoevg.maftimer.presenter.screens.sections.player.components.ui.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.maftimer.presenter.screens.sections.player.MusicPlayerState

@Composable
fun SongInfo(
    state: MusicPlayerState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoverAlbumImage(state)
        // Текст исполнитель / название песни
        Column(modifier = Modifier.padding(start = 6.dp)) {
            Text(
                text = state.artist,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = state.title,
                color = Color.LightGray,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SongInfoPreview() {
    SongInfo(
        state = MusicPlayerState(
            isAuthorized = true,
            artist = "Ivo Bobul",
            title = "Balalay",
            isPlaying = false,
            progressMs = 125000L,
            durationMs = 180000L
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun SongInfoLongPreview() {
    SongInfo(
        state = MusicPlayerState(
            isAuthorized = true,
            artist = "ой у вишневому саду там соловейко",
            title = "Очень длинное название песни которое нужно обрезать",
            isPlaying = false,
            progressMs = 125000L,
            durationMs = 180000L
        )
    )
}