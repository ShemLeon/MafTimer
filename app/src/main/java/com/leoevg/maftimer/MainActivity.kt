package com.leoevg.maftimer


import com.leoevg.maftimer.presentation.util.Logx
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.leoevg.maftimer.data.repository.SpotifyRepository
import com.leoevg.maftimer.presentation.navigation.MainNavHost
import com.leoevg.maftimer.presentation.util.HideSystemBars
import com.leoevg.maftimer.ui.theme.MafTimerTheme
import com.leoevg.maftimer.presentation.util.SpotifyAuthManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var spotifyAuthManager: SpotifyAuthManager

    @Inject
    lateinit var spotifyRepository: SpotifyRepository

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logx.info(TAG, "onCreate")

        // Проверяем intent при запуске
        intent?.let {
            Logx.info(TAG, "Launch intent: ${it.data}")
            handleIntent(it)
        }

        setContent {
            MafTimerTheme {
                HideSystemBars() // для скрытия системных кнопок
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    MainNavHost()
                }
            }
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Logx.info(TAG, "onNewIntent: ${intent.data}")
        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        Logx.info(TAG, "onResume")

        // Принудительно проверить авторизацию при возврате
        val prefs = getSharedPreferences("spotify_auth", Context.MODE_PRIVATE)
        val token = prefs.getString("access_token", null)
        if (token != null) {
            Logx.success(TAG, "Token present in prefs - setting to repository")
            spotifyRepository.setAccessToken(token)
        } else {
            Logx.error(TAG, "No token in prefs")
        }
    }

    override fun onPause() {
        super.onPause()
        Logx.info(TAG, "onPause")
    }

    private fun handleIntent(intent: Intent) {
        if (intent.data?.scheme == "com.leoevg.maftimer") {
            Logx.action(TAG, "Handling Spotify callback")
            spotifyAuthManager.handleAuthResponse(intent)
        }
    }
}