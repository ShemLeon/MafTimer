package com.leoevg.maftimer.presentation.screens.main

import com.leoevg.maftimer.presentation.screens.sections.player.MusicPlayerState
import com.leoevg.maftimer.presentation.screens.sections.player.components.ui.MusicAssembly
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.leoevg.maftimer.presentation.navigation.NavigationPaths
import com.leoevg.maftimer.presentation.screens.sections.title.TitleApplication
import com.leoevg.maftimer.presentation.screens.sections.player.MusicPlayer
import com.leoevg.maftimer.presentation.screens.sections.timer.Timer
import com.leoevg.maftimer.presentation.screens.sections.timer.TimerState
import com.leoevg.maftimer.presentation.screens.sections.timer.components.TimerAssembly

private const val TAG = "MainScreen"

@Composable
fun MainScreen(
    navigate: (NavigationPaths) -> Unit
) {
    val topGradientColor = Color(0xFF111827)
    val middleGradientColor = Color(0xFF1F2937)
    val bottomGradientColor = Color(0xFF111827)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(topGradientColor, middleGradientColor, bottomGradientColor)
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleApplication()
            Timer()
            Spacer(Modifier.weight(1f))
            MusicPlayer(navigate = navigate)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun MainScreenContentPreview() {
    val topGradientColor = Color(0xFF111827)
    val middleGradientColor = Color(0xFF1F2937)
    val bottomGradientColor = Color(0xFF111827)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        topGradientColor,
                        middleGradientColor,
                        bottomGradientColor
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleApplication()
            // Используем TimerAssembly напрямую для превью
            TimerAssembly(
                state = TimerState(
                    totalSeconds = 60,
                    progressFraction = 0.3f,
                    isRunning = false,
                    isPaused = false,
                    isFinished = false
                ),
                onEvent = {}
            )
            Spacer(Modifier.weight(1f))
            // Используем MusicAssembly напрямую для превью
            MusicAssembly(
                state = MusicPlayerState(),
                onEvent = {},
                onSpotifyAuthRequest = {}
            )
        }
    }
}


