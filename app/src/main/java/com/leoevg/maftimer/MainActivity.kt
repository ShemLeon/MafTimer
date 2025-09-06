package com.leoevg.maftimer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.leoevg.maftimer.data.repository.SpotifyRepository
import com.leoevg.maftimer.navigation.MainNavHost
import com.leoevg.maftimer.presenter.util.HideSystemBars
import com.leoevg.maftimer.ui.theme.MafTimerTheme
import com.leoevg.maftimer.presenter.util.SpotifyAuthManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var spotifyAuthManager: SpotifyAuthManager
    @Inject
    lateinit var spotifyRepository: SpotifyRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")

          // Устанавливаем callback для получения токена
        spotifyAuthManager.onTokenReceived = { token ->
            Log.d("MainActivity", "Token received in callback: ${token.take(10)}...")
            spotifyRepository.setAccessToken(token)
        }

        // Проверяем intent при запуске
        intent?.let {
            Log.d("MainActivity", "Launch intent: ${it.data}")
            handleIntent(it)
        }

        setContent {
            MafTimerTheme {
                HideSystemBars() // для скрытия системных кнопок
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    MainNavHost(
                        onSpotifyAuthRequest = {
                            spotifyAuthManager.startAuth(this)
                        }
                    )
                }
            }
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("MainActivity", "onNewIntent called with: ${intent.data}")
        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called")
        // Принудительно проверить авторизацию при возврате
        // Это временное решение для тестирования
        val prefs = getSharedPreferences("spotify_auth", Context.MODE_PRIVATE)
        val token = prefs.getString("access_token", null)
        Log.d("MainActivity", "onResume: token = ${if (token != null) "found" else "null"}")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause called")
    }

    private fun handleIntent(intent: Intent) {
        if (intent.data?.scheme == "com.leoevg.maftimer") {
            Log.d("MainActivity", "Handling Spotify callback")
            spotifyAuthManager.handleAuthResponse(intent)
        }
    }
}