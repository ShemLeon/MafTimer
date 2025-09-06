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
import com.leoevg.maftimer.presenter.util.Logx


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
            Logx.success(TAG, "Token found: ${token.take(10)}... (length: ${token.length})")
        } else {
            Logx.error(TAG, "No stored token found")
        }
        return token
    }

    fun saveToken(token: String) {
        Logx.storage(TAG, "Saving token: ${token.take(10)}... (length: ${token.length})")
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
        Logx.success(TAG, "Token saved successfully")
    }

    fun clearToken() {
        Logx.storage(TAG, "Clearing stored token")
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
        Logx.success(TAG, "Token cleared successfully")
    }

    fun startAuth(activity: ComponentActivity) {
        val authUrl = "https://accounts.spotify.com/authorize?" +
                "client_id=$CLIENT_ID" +
                "&response_type=code" +
                "&redirect_uri=$REDIRECT_URI" +
                "&scope=$SCOPES" +
                "&show_dialog=true"  // Принудительно показать диалог авторизации

        Logx.action(TAG, "Starting Spotify authorization")
        Logx.info(TAG, "🔗 Auth URL: $authUrl")

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
            // Принудительно открыть в браузере, а не в Spotify приложении
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            activity.startActivity(intent)
        } catch (e: Exception) {
            Logx.error(TAG, "Failed to start auth", e)
        }
    }

    fun handleAuthResponse(intent: Intent): String? {
        val uri = intent.data
        Logx.info(TAG, "Handling auth response: $uri")

        if (uri != null && uri.scheme == "com.leoevg.maftimer") {
            val code = uri.getQueryParameter("code")
            val error = uri.getQueryParameter("error")

            Logx.info(TAG, "Auth response → code=$code, error=$error")

            if (error != null) {
                Logx.error(TAG, "Auth error: $error")
                return null
            }

            if (code != null) {
                Logx.action(TAG, "Authorization code received → exchanging for token…")
                exchangeCodeForToken(code)
                return code
            } else {
                Logx.error(TAG, "No code found in response")
            }
        } else {
            Logx.error(TAG, "Invalid URI scheme or null URI")
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

                Logx.network(TAG, "Exchanging code for token…")

                httpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        Logx.network(TAG, "Token response: $responseBody")

                        if (responseBody != null) {
                            val json = JSONObject(responseBody)
                            val accessToken = json.getString("access_token")

                            Logx.success(TAG, "Token exchange successful")
                            saveToken(accessToken)

                            // Уведомляем о получении токена
                            CoroutineScope(Dispatchers.Main).launch {
                                Logx.debug(
                                    TAG,
                                    "Calling onTokenReceived callback: fun isNull=${onTokenReceived == null}"
                                )
                                onTokenReceived?.invoke(accessToken)
                            }
                        }
                    } else {
                        Logx.error(TAG, "Token exchange failed: ${response.code} - ${response.message}")
                        val errorBody = response.body?.string()
                        Logx.error(TAG, "Error body: $errorBody")
                    }
                }
            } catch (e: Exception) {
                Logx.error(TAG, "Exception during token exchange", e)
            }
        }
    }

    fun clearTokenForTesting() {
        Logx.storage(TAG, "🧪 Clearing token for testing")
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
        Logx.success(TAG, "Token cleared for testing")
    }
}