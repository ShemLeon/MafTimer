package com.leoevg.maftimer.presenter.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
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
}