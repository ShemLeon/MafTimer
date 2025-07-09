package com.leoevg.maftimer.screens.timer


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.leoevg.maftimer.customUI.CustomCircle
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import com.leoevg.maftimer.customUI.PlayerContainer

@Composable
fun TimerScreen(
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
        Row(
            modifier = Modifier.padding(10.dp)
        ){
            Text(
            text = "Timer Screen",
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            fontSize = 30.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
            )
        }
// блок с Часами
        Row(
            modifier = Modifier
                .offset(y = -(screenHeightDp * 0.15f))
        ){
            CustomCircle(color = Color.DarkGray, radiusSize = 0.5f)
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

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
            Row(
                modifier = Modifier
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Gray))
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.White))
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.Gray))
            }
            PlayerContainer()
        }
    }
}



@Composable
@Preview(showBackground = true)
fun TimerScreenPreview() {
    TimerScreen(navigate = {})
}