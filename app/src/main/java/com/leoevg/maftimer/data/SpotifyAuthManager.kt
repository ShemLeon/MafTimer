package com.leoevg.maftimer.data

import android.content.Context
import com.leoevg.maftimer.data.AuthState.*
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyAuthManager @Inject constructor() {
    companion object{
        const val CLIENT_ID = "1aff82fd294d4823aaed14ef398a9714"
        const val REDIRECT_URI = "com.leoevg.maftimer://callback"
        const val AUTH_TOKEN_REQUEST_CODE = 0x10
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.NotAuthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun authenticate(context: Context) {
        val builder = AuthorizationRequest.Builder(
            CLIENT_ID,
            AuthorizationResponse.Type.TOKEN,
            REDIRECT_URI
        )

        builder.setScopes(arrayOf(
            "streaming",
            "user-read-email",
            "user-read-private",
            "playlist-read-private",
            "playlist-modify-public",
            "playlist-modify-private"
        ))

        val request = builder.build()
        AuthorizationClient.openLoginActivity(
            context as android.app.Activity,
            AUTH_TOKEN_REQUEST_CODE,
            request
        )
    }

    fun handleAuthResponse(response: AuthorizationResponse) {
        when (response.type) {
            AuthorizationResponse.Type.TOKEN -> {
                val accessToken = response.accessToken
                _authState.value = Authenticated(accessToken)
            }
            AuthorizationResponse.Type.ERROR -> {
                _authState.value = Error(response.error)
            }
            AuthorizationResponse.Type.EMPTY -> {
                _authState.value = AuthState.NotAuthenticated
            }

            AuthorizationResponse.Type.CODE -> _authState.value = Error("Code response not handled")
            AuthorizationResponse.Type.UNKNOWN -> _authState.value = Error("Unknown response")
        }
    }

    fun logout() {
        _authState.value = AuthState.NotAuthenticated
    }

}

sealed class AuthState {
    object NotAuthenticated : AuthState()
    data class Authenticated(val accessToken: String) : AuthState()
    data class Error(val error: String) : AuthState()
}