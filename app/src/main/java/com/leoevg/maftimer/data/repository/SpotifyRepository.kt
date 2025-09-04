package com.leoevg.maftimer.data.repository

import android.util.Log
import com.leoevg.maftimer.data.api.SpotifyApi
import com.leoevg.maftimer.data.api.SpotifyPlaybackState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyRepository @Inject constructor(
    private val spotifyApi: SpotifyApi
) {
    private val _playbackState = MutableStateFlow<SpotifyPlaybackState?>(null)
    val playbackState: StateFlow<SpotifyPlaybackState?> = _playbackState.asStateFlow()

    private var accessToken: String? = null

    fun setAccessToken(token: String) {
        accessToken = token
        Log.d("SpotifyRepository", "Token set: ${token.take(10)}...")
    }

    private fun getAuthHeader(): String = "Bearer $accessToken"

    suspend fun getCurrentPlayback(): Result<SpotifyPlaybackState?> {
        return try {
            if (accessToken == null) {
                Log.e("SpotifyRepository", "No access token available")
                return Result.failure(Exception("No access token"))
            }

            Log.d("SpotifyRepository", "Getting current playback...")
            val response = spotifyApi.getCurrentPlayback(getAuthHeader())

            if (response.isSuccessful) {
                val playbackState = response.body()
                _playbackState.value = playbackState
                Log.d("SpotifyRepository", "Playback state updated: ${playbackState?.is_playing}")
                Result.success(playbackState)
            } else {
                Log.e("SpotifyRepository", "Failed to get playback state: ${response.code()} - ${response.message()}")
                Result.failure(Exception("Failed to get playback state: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("SpotifyRepository", "Exception getting playback state", e)
            Result.failure(e)
        }
    }

    suspend fun play(): Result<Unit> {
        return try {
            if (accessToken == null) {
                return Result.failure(Exception("No access token"))
            }

            Log.d("SpotifyRepository", "Playing...")
            val response = spotifyApi.resumePlayback(getAuthHeader())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Log.e("SpotifyRepository", "Failed to play: ${response.code()}")
                Result.failure(Exception("Failed to play: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("SpotifyRepository", "Exception playing", e)
            Result.failure(e)
        }
    }

    suspend fun pause(): Result<Unit> {
        return try {
            if (accessToken == null) {
                return Result.failure(Exception("No access token"))
            }

            Log.d("SpotifyRepository", "Pausing...")
            val response = spotifyApi.pausePlayback(getAuthHeader())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Log.e("SpotifyRepository", "Failed to pause: ${response.code()}")
                Result.failure(Exception("Failed to pause: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("SpotifyRepository", "Exception pausing", e)
            Result.failure(e)
        }
    }

    suspend fun next(): Result<Unit> {
        return try {
            if (accessToken == null) {
                return Result.failure(Exception("No access token"))
            }

            Log.d("SpotifyRepository", "Skipping to next...")
            val response = spotifyApi.skipToNext(getAuthHeader())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Log.e("SpotifyRepository", "Failed to skip next: ${response.code()}")
                Result.failure(Exception("Failed to skip next: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("SpotifyRepository", "Exception skipping next", e)
            Result.failure(e)
        }
    }

    suspend fun previous(): Result<Unit> {
        return try {
            if (accessToken == null) {
                return Result.failure(Exception("No access token"))
            }
            Log.d("SpotifyRepository", "Skipping to previous...")
            val response = spotifyApi.skipToPrevious(getAuthHeader())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Log.e("SpotifyRepository", "Failed to skip previous: ${response.code()}")
                Result.failure(Exception("Failed to skip previous: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("SpotifyRepository", "Exception skipping previous", e)
            Result.failure(e)
        }
    }

    suspend fun seekTo(positionMs: Long): Result<Unit> {
        return try {
            if (accessToken == null) {
                return Result.failure(Exception("No access token"))
            }

            Log.d("SpotifyRepository", "Seeking to $positionMs...")
            val response = spotifyApi.seekToPosition(getAuthHeader(), positionMs)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Log.e("SpotifyRepository", "Failed to seek: ${response.code()}")
                Result.failure(Exception("Failed to seek: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("SpotifyRepository", "Exception seeking", e)
            Result.failure(e)
        }
    }
}