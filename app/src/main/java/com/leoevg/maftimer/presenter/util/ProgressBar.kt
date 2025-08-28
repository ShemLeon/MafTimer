package com.leoevg.maftimer.presenter.util

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun ProgressBar(
    percentage: Float,          // Процент заполнения (0.0 - 1.0)
    number: Int,                // Число для отображения
    fontSize: TextUnit = 28.sp,
    radius: Dp = 50.dp,         // Радиус круга
    color: Color = Color.Green, // Цвет прогресса
    strokeWidth: Dp = 8.dp,     // Толщина линии
    animDuration: Int = 1000,   // Длительность анимации (мс)
    animDelay: Int = 0           // Задержка перед анимацией (мс)
) {
    val curPercentage = percentage
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
//                color = Color.LightGray,
                color = if (curPercentage >= (50f / 60f)) Color(0xFFFF3B30) else Color.LightGray,
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
                sweepAngle = -360 * curPercentage,
                useCenter = false,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Butt),
                topLeft = Offset(centerX - radiusPx, centerY - radiusPx),
                size = Size(radiusPx * 2, radiusPx * 2)
            )
        }

        Text(
            modifier = Modifier
                .padding(top = 200.dp)
                .align(Alignment.Center),
            text = (number - (curPercentage * number)).toInt().toString(),
            color = Color.Black,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CircularProgressBarPreview() {
    ProgressBar(
        percentage = 0.75f,
        number = 100,
        color = Color.Green,
        strokeWidth = 12.dp,
        animDuration = 1200
    )
}