package com.leoevg.maftimer.presenter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Основной контейнер плеера
@Composable
fun PlayerContainer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xCC424242)) // Полупрозрачный темно-серый
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Строка 1: Информация о песне
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Заглушка для обложки альбома
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF5A5A5A))
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                Text("Кухни", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Text("Бонд с кнопкой", color = Color.LightGray, fontSize = 16.sp)
            }
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