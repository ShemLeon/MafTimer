package com.leoevg.maftimer.presenter.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Timer(
    totalTime: Long,
    handleColor: Color,
    inactiveBarColor: Color,
    activeBarColor: Color,
    modifier: Modifier = Modifier,
    initialValue: Float = 0f,
    strokeWidth: Dp = 5.dp
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    var value by remember {
        mutableStateOf(0f)
    }
    var currentTime by remember {
        mutableStateOf(totalTime)
    }
    var isTimerRunning by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = isTimerRunning) {
        while (currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            value = 1f - (currentTime / totalTime.toFloat())
        }
        if (currentTime <= 0) {
            isTimerRunning = false
            value = 1f
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .onSizeChanged {
                size = it
            }
    ) {
        Canvas(modifier = modifier.matchParentSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = (size.width.coerceAtMost(size.height) / 2f) - strokeWidth.toPx()

            // Рисуем неактивную дугу (фон)
            drawArc(
                color = inactiveBarColor,
                startAngle = -250f,
                sweepAngle = 250f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Butt)
            )

            // Рисуем активную дугу (прогресс)
            drawArc(
                color = activeBarColor,
                startAngle = -250f,
                sweepAngle = 250f * value,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Butt)
            )

            // Рисуем указатель
            val beta = (250f * value - 250f) * (PI / 180f).toFloat()
            val a = cos(beta) * radius
            val b = sin(beta) * radius

            drawPoints(
                listOf(Offset(center.x + a, center.y + b)),
                pointMode = PointMode.Points,
                color = handleColor,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round
            )
        }

        // Текст времени
        Text(
            text = (currentTime / 1000L).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )

        // Кнопка управления
        Button(
            onClick = {
                if (currentTime <= 0L) {
                    currentTime = totalTime
                    value = 0f
                    isTimerRunning = true
                } else {
                    isTimerRunning = !isTimerRunning
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isTimerRunning || currentTime <= 0L) {
                    Color.Green
                } else {
                    Color.Red
                }
            )
        ) {
            Text(
                text = when {
                    currentTime <= 0L -> "Restart"
                    isTimerRunning -> "Stop"
                    else -> "Start"
                },
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerPreview() {
    Timer(
        totalTime = 60000L,
        handleColor = Color.Blue,
        inactiveBarColor = Color.Gray,
        activeBarColor = Color.Green,
        initialValue = 0.5f,
        strokeWidth = 8.dp
    )
}