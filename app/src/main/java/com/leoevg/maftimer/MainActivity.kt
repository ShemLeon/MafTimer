package com.leoevg.maftimer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.leoevg.maftimer.data.SpotifyAuthManager
import com.leoevg.maftimer.navigation.MainNavHost
import com.leoevg.maftimer.presenter.theme.MafTimerTheme
import com.leoevg.maftimer.presenter.util.HideSystemBars
import com.leoevg.maftimer.ui.theme.MafTimerTheme
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.spotify.sdk.android.appremote.api.ConnectionParams
import com.spotify.sdk.android.appremote.api.Connector
import com.spotify.sdk.android.appremote.api.SpotifyAppRemote

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var spotifyAuthManager: SpotifyAuthManager

    private val authLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if (result.resultCode == RESULT_OK){
            val response = AuthorizationClient
                .getResponse(
                    result.resultCode,
                    result.data
                )
            spotifyAuthManager.handleAuthResponse(response)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle deep link callback
        handleIntent(intent)

        setContent {
            MafTimerTheme {
                MainNavHost(
                    modifier = Modifier.fillMaxSize()
                )
                HideSystemBars()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.data?.let { uri ->
            if (uri.scheme == "com.leoevg.maftimer") {
                // Handle Spotify callback
                val response = AuthorizationClient.getResponse(
                    SpotifyAuthManager.AUTH_TOKEN_REQUEST_CODE,
                    intent
                )
                spotifyAuthManager.handleAuthResponse(response)
            }
        }
    }
}