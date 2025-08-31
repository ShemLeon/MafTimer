package com.leoevg.maftimer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.leoevg.maftimer.data.repository.SpotifyRepository
import com.leoevg.maftimer.navigation.MainNavHost
import com.leoevg.maftimer.ui.theme.MafTimerTheme
import com.leoevg.maftimer.util.SpotifyAuthManager
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
        enableEdgeToEdge()
        setContent {
            MafTimerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavHost(
                        modifier = Modifier.padding(innerPadding),
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
        if (intent.data?.scheme == "com.leoevg.maftimer") {
            val token = spotifyAuthManager.handleAuthResponse(intent)
            token?.let { accessToken ->
                // Передаем токен в репозиторий
                spotifyRepository.setAccessToken(accessToken)
            }
        }
    }
}