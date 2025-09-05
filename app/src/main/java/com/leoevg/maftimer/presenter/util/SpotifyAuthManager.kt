package com.leoevg.maftimer.presenter.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyAuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val CLIENT_ID = "1aff82fd294d4823aaed14ef398a9714"
        private const val CLIENT_SECRET = "d640b8151ef94e668766b0a59d50c39e"
        private const val REDIRECT_URI = "com.leoevg.maftimer://callback"
        private const val PREFS_NAME = "spotify_auth"
        private const val TOKEN_KEY = "access_token"

        private val SCOPES = listOf(
            "user-read-playback-state",
            "user-modify-playback-state",
            "user-read-currently-playing"
        ).joinToString("%20")
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val httpClient = OkHttpClient()

    var onTokenReceived: ((String) -> Unit)? = null

    fun getStoredToken(): String? {
        val token = sharedPreferences.getString(TOKEN_KEY, null)
        Log.d("SpotifyAuthManager", "Getting stored token: ${if (token != null) "found" else "not found"}")
        return token
    }

    fun saveToken(token: String) {
        Log.d("SpotifyAuthManager", "Saving token: ${token.take(10)}...")
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun clearToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
    }

    fun startAuth(activity: ComponentActivity) {
        val authUrl = "https://accounts.spotify.com/authorize?" +
                "client_id=$CLIENT_ID" +
                "&response_type=code" +
                "&redirect_uri=$REDIRECT_URI" +
                "&scope=$SCOPES" +
                "&show_dialog=true"  // Принудительно показать диалог авторизации

        Log.d("SpotifyAuthManager", "Starting auth with URL: $authUrl")

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
            // Принудительно открыть в браузере, а не в Spotify приложении
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            activity.startActivity(intent)
        } catch (e: Exception) {
            Log.e("SpotifyAuthManager", "Failed to start auth", e)
        }
    }

    fun handleAuthResponse(intent: Intent): String? {
        val uri = intent.data
        Log.d("SpotifyAuthManager", "Handling auth response: $uri")

        if (uri != null && uri.scheme == "com.leoevg.maftimer") {
            val code = uri.getQueryParameter("code")
            val error = uri.getQueryParameter("error")

            Log.d("SpotifyAuthManager", "Code: $code, Error: $error")

            if (error != null) {
                Log.e("SpotifyAuthManager", "Auth error: $error")
                return null
            }

            if (code != null) {
                Log.d("SpotifyAuthManager", "Authorization code received, exchanging for token...")
                exchangeCodeForToken(code)
                return code
            } else {
                Log.e("SpotifyAuthManager", "No code found in response")
            }
        } else {
            Log.e("SpotifyAuthManager", "Invalid URI scheme or null URI")
        }
        return null
    }

    private fun exchangeCodeForToken(code: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Создаем Basic Auth заголовок вручную
                val credentials = Base64.encodeToString(
                    "$CLIENT_ID:$CLIENT_SECRET".toByteArray(),
                    Base64.NO_WRAP
                )

                val formBody = FormBody.Builder()
                    .add("grant_type", "authorization_code")
                    .add("code", code)
                    .add("redirect_uri", REDIRECT_URI)
                    .build()

                val request = Request.Builder()
                    .url("https://accounts.spotify.com/api/token")
                    .header("Authorization", "Basic $credentials")
                    .post(formBody)
                    .build()

                Log.d("SpotifyAuthManager", "Exchanging code for token...")

                httpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        Log.d("SpotifyAuthManager", "Token response: $responseBody")

                        if (responseBody != null) {
                            val json = JSONObject(responseBody)
                            val accessToken = json.getString("access_token")

                            Log.d("SpotifyAuthManager", "Token exchange successful!")
                            saveToken(accessToken)

                            // Уведомляем о получении токена
                            CoroutineScope(Dispatchers.Main).launch {
                                Log.d(
                                    "SpotifyAuthManager",
                                    "Calling onTokenReceived callback: fun isNull =${onTokenReceived==null}"
                                )
                                onTokenReceived?.invoke(accessToken)
                            }
                        }
                    } else {
                        Log.e("SpotifyAuthManager", "Token exchange failed: ${response.code} - ${response.message}")
                        val errorBody = response.body?.string()
                        Log.e("SpotifyAuthManager", "Error body: $errorBody")
                    }
                }
            } catch (e: Exception) {
                Log.e("SpotifyAuthManager", "Exception during token exchange", e)
            }
        }
    }
}