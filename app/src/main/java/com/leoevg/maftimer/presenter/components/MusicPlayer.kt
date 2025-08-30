package com.leoevg.maftimer.presenter.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.maftimer.R
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

// Основной контейнер плеера
@Composable
fun PlayerContainer(
    singer: String = "Maser",
    title: String = "На магистрейте",
) {
    // В вашем Composable функции:
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xCC424242)) // Полупрозрачный темно-серый
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Строка 1: Информация о песне
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // example for album title
            Image(
                painter = painterResource(R.drawable.ernest),
                contentDescription = "Обложка альбома",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(start = 18.dp)
            ) {
                Text(text = singer, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
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
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("spotify://"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Если Spotify не установлен, показываем диалог
                            showDialog = true
                        }
                    },
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Строка 2: Слайдер и время
        Column(modifier = Modifier.fillMaxWidth()) {
            Slider(
                value = 0.34f, // Примерное значение, как на скриншоте
                onValueChange = { /* TODO */ },
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.Gray.copy(alpha = 0.5f)
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("0:29", color = Color.LightGray, fontSize = 12.sp)
                Text("-1:51", color = Color.LightGray, fontSize = 12.sp)
            }
        }


    }
}

@Composable
@Preview(showBackground = true)
fun MusicPlayerPreview() {
    PlayerContainer()
}

@Composable
fun PlayerContainer(
    trackName: String,
    artist: String,
    progress: Float, // от 0 до 1
    isPlaying: Boolean,
    onPlayPause: () -> Unit
) {
    // Используй эти параметры для обновления Text, Slider и кнопок
    // Например, в Slider: value = progress
}