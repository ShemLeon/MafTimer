package com.leoevg.maftimer.presenter.screens.main


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.leoevg.maftimer.presenter.ui.CircularProgressBar
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoevg.maftimer.navigation.NavigationPaths
import androidx.compose.ui.platform.LocalConfiguration
import com.leoevg.maftimer.presenter.ui.CustomCircle
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import com.leoevg.maftimer.presenter.ui.CircularProgressBar
import com.leoevg.maftimer.presenter.ui.Indicators
import com.leoevg.maftimer.presenter.ui.PlayerContainer
import com.leoevg.maftimer.presenter.ui.Timer

@Composable
fun MainScreen(
    navigate: (NavigationPaths) -> Unit
) {
    // Определяем цвета для градиента
    val topGradientColor = Color(0xFF3B3736) // Более светлый оттенок (сверху)
    val bottomGradientColor = Color(0xFF292625) // Более темный оттенок (снизу)
// Извлекаем высоту экрана в Dp
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    Box(
        modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(topGradientColor, bottomGradientColor)
            )
        )
        .padding(5.dp)
    ){
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
                    CircularProgressBar(
                        percentage = 0.65f,
                        number = 100,
                        color = Color.Green,
                        strokeWidth = 12.dp
                    )
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



@Composable
@Preview(showBackground = true)
fun TimerScreenPreview() {
    MainScreen(navigate = {})
}