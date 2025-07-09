package com.leoevg.maftimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.leoevg.maftimer.navigation.MainNavHost
import com.leoevg.maftimer.ui.theme.MafTimerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HideSystemBars()

            MafTimerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainNavHost(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun HideSystemBars() {
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
