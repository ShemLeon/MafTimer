package com.leoevg.maftimer.presenter.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.leoevg.maftimer.presenter.util.ProgressBar
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.maftimer.navigation.NavigationPaths
import androidx.compose.ui.platform.LocalConfiguration
import com.leoevg.maftimer.presenter.util.CustomCircle
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.leoevg.maftimer.presenter.util.Indicators
import com.leoevg.maftimer.presenter.util.PlayerContainer
import com.leoevg.maftimer.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.leoevg.maftimer.presenter.util.DialDivider

@Composable
fun MainScreen(
    navigate: (NavigationPaths) -> Unit
) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val progress by viewModel.progressFraction.collectAsState()

    MainScreenContent(
        progress = progress,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun MainScreenContent(
    progress: Float,
    onEvent: (MainScreenEvent) -> Unit
) {
    // Извлекаем высоту экрана в Dp
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    // Определяем цвета для градиента
    val topGradientColor = Color(0xFF3B3736) // Более светлый оттенок (сверху)
    val bottomGradientColor = Color(0xFF292625) // Более темный оттенок (снизу)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        topGradientColor,
                        bottomGradientColor
                    )
                )
            )
            .padding(5.dp)
    ) {
        Column() {
            // Блок с title
            Row(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = "Timer Screen",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 30.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            // Блок с кругом
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .offset(y = -(screenHeightDp * 0.15f))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(1f)
                ) {
                    CustomCircle(
                        color = Color.White,
                        diameterFraction = 1f
                    )
                    ProgressBar(
                        percentage = progress, // сектор от 60
                        number = 60,           // текст внутри = seconds
                        color = Color.Green,
                        strokeWidth = 12.dp
                    )
                    // Кнопка управления
                    IconButton(
                        modifier = Modifier
                            .fillMaxSize(1f)
                            .align(Alignment.Center)
                            .padding(start = 25.dp),
                        onClick = {
                            onEvent(MainScreenEvent.OnBtnTimerStartClick)
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.btn_start),
                            contentDescription = "Start",
                            tint = Color.Red,
                            modifier = Modifier
                                .fillMaxSize(0.45f),
                        )
                    }
                    // Разделители
                    DialDivider(angleDegrees = 0, color = Color(0x80000000))
                    DialDivider(angleDegrees = 180, color = Color(0xFF3D5AFE))
                    DialDivider(angleDegrees = 60, color = Color(0x80fc520d))

                }
            }
        }

        // Блок для плеера
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Три маленьких кружочка сверху
            Indicators()
            PlayerContainer()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    MainScreenContent(
        progress = 0.3f,
        onEvent = { it -> }
    )
}