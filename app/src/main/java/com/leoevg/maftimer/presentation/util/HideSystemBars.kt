package com.leoevg.maftimer.presentation.util

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

// Функция для скрытия системных баров (нижние кнопки)
@Composable
fun HideSystemBars() {
    val view = LocalView.current
    DisposableEffect(view) {
        val window = (view.context as ComponentActivity).window
        val insetsController = WindowCompat.getInsetsController(window, view)

        // Прячем и статус-бар, и навигационную панель
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        // Устанавливаем поведение: системные панели появятся по свайпу от края
        // и скроются обратно через короткое время
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        onDispose {
            // Показываем панели обратно, когда Composable-компонент уходит с экрана
            // (на случай если в приложении будут другие экраны, где панели нужны)
            insetsController.show(WindowInsetsCompat.Type.systemBars())
        }
    }
}
