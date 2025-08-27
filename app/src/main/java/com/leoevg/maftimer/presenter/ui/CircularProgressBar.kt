package com.leoevg.maftimer.presenter.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CircularProgressBar(
    percentage: Float,          // Процент заполнения (0.0 - 1.0)
    number: Int,                // Число для отображения
    fontSize: TextUnit = 28.sp,
    radius: Dp = 50.dp,         // Радиус круга
    color: Color = Color.Green, // Цвет прогресса
    strokeWidth: Dp = 8.dp,     // Толщина линии
    animDuration: Int = 1000,   // Длительность анимации (мс)
    animDelay: Int = 0           // Задержка перед анимацией (мс)
) {
    // Переменная для отслеживания, была ли уже запущена анимация
    // remember сохраняет значение между перекомпозициями
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    // animateFloatAsState создает анимированное значение,
    // начинает с 0 и анимируется до percentage
    // Использует tween для плавной анимации
    val curPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )
    // LaunchedEffect запускается один раз при первом рендере
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidthPx = strokeWidth.toPx()
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radiusPx = minOf(size.width, size.height) / 2 - strokeWidthPx / 2

            // Фоновый круг
            drawArc(
                color = Color.LightGray,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
                topLeft = Offset(centerX - radiusPx, centerY - radiusPx),
                size = Size(radiusPx * 2, radiusPx * 2)
            )

            // Прогресс-дуга
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * curPercentage.value,
                useCenter = false,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
                topLeft = Offset(centerX - radiusPx, centerY - radiusPx),
                size = Size(radiusPx * 2, radiusPx * 2)
            )
        }

        Text(
            text = (curPercentage.value * number).toInt().toString(),
            color = Color.Black,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CircularProgressBarPreview() {
    CircularProgressBar(
        percentage = 0.75f,
        number = 100,
        color = Color.Green,
        strokeWidth = 12.dp,
        animDuration = 1200
    )
}