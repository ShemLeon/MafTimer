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
        private const val TAG = "SpotifyAuthManager"
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
        if (token != null) {
            Log.d("TAG", "✅ Token found: ${token.take(10)}... (length: ${token.length})")
        } else {
            Log.d("TAG", "❌ No stored token found")
        }
        return token
    }

    fun saveToken(token: String) {
        Log.d("TAG", "💾 Saving token: ${token.take(10)}... (length: ${token.length})")
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
        Log.d("TAG", "✅ Token saved successfully")
    }

    fun clearToken() {
        Log.d("TAG", "🗑️ Clearing stored token")
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
        Log.d("TAG", "✅ Token cleared successfully")
    }

    fun startAuth(activity: ComponentActivity) {
        val authUrl = "https://accounts.spotify.com/authorize?" +
                "client_id=$CLIENT_ID" +
                "&response_type=code" +
                "&redirect_uri=$REDIRECT_URI" +
                "&scope=$SCOPES" +
                "&show_dialog=true"  // Принудительно показать диалог авторизации

        Log.d("TAG", "🚀 Starting Spotify authorization")
        Log.d("TAG", "�� Auth URL: $authUrl")

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
            // Принудительно открыть в браузере, а не в Spotify приложении
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            activity.startActivity(intent)
        } catch (e: Exception) {
            Log.e("TAG", "Failed to start auth", e)
        }
    }

    fun handleAuthResponse(intent: Intent): String? {
        val uri = intent.data
        Log.d("TAG", "Handling auth response: $uri")

        if (uri != null && uri.scheme == "com.leoevg.maftimer") {
            val code = uri.getQueryParameter("code")
            val error = uri.getQueryParameter("error")

            Log.d("TAG", "Code: $code, Error: $error")

            if (error != null) {
                Log.e("TAG", "Auth error: $error")
                return null
            }

            if (code != null) {
                Log.d("TAG", "Authorization code received, exchanging for token...")
                exchangeCodeForToken(code)
                return code
            } else {
                Log.e("TAG", "No code found in response")
            }
        } else {
            Log.e("TAG", "Invalid URI scheme or null URI")
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

                Log.d("TAG", "Exchanging code for token...")

                httpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        Log.d("TAG", "Token response: $responseBody")

                        if (responseBody != null) {
                            val json = JSONObject(responseBody)
                            val accessToken = json.getString("access_token")

                            Log.d("TAG", "Token exchange successful!")
                            saveToken(accessToken)

                            // Уведомляем о получении токена
                            CoroutineScope(Dispatchers.Main).launch {
                                Log.d(
                                    "TAG",
                                    "Calling onTokenReceived callback: fun isNull =${onTokenReceived==null}"
                                )
                                onTokenReceived?.invoke(accessToken)
                            }
                        }
                    } else {
                        Log.e("TAG", "Token exchange failed: ${response.code} - ${response.message}")
                        val errorBody = response.body?.string()
                        Log.e("TAG", "Error body: $errorBody")
                    }
                }
            } catch (e: Exception) {
                Log.e("TAG", "Exception during token exchange", e)
            }
        }
    }

    fun clearTokenForTesting() {
        Log.d("SpotifyAuthManager", "�� Clearing token for testing")
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
        Log.d("SpotifyAuthManager", "✅ Token cleared for testing")
    }
}