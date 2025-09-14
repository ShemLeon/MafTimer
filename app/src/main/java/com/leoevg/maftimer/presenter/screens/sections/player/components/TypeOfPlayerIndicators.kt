package com.leoevg.maftimer.presenter.screens.sections.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoevg.maftimer.ui.theme.MafTimerTheme

@Composable
fun TypeOfPlayerIndicators(selectedPage: Int = 1){
    Row(
        modifier = Modifier
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        // Left circle for local (selectedPage = 0)
        Box(modifier = Modifier.size(8.dp).clip(CircleShape)
            .background(if (selectedPage == 0) Color(color = 0xef00ffff) else Color.Gray))
        Box(modifier = Modifier.size(8.dp).clip(CircleShape)
            .background(if (selectedPage == 1) Color.White else Color.Gray))
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF121212)
fun IndicatorsLocalPreview() {
    MafTimerTheme {
        TypeOfPlayerIndicators(selectedPage = 0)  // Left white, right gray
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF121212)
fun IndicatorsSpotifyPreview() {
    MafTimerTheme {
        TypeOfPlayerIndicators(selectedPage = 1)  // Left gray, right white
    }
}