package com.leoevg.maftimer.data.repository

import com.leoevg.maftimer.presenter.util.Logx
import android.util.Log
import com.leoevg.maftimer.data.api.SpotifyApi
import com.leoevg.maftimer.data.api.SpotifyPlaybackState
import com.leoevg.maftimer.domain.repository.ISpotifyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyRepository @Inject constructor(
    private val spotifyApi: SpotifyApi
): ISpotifyRepository {
    private val _playbackState = MutableStateFlow<SpotifyPlaybackState?>(null)
    val playbackState: StateFlow<SpotifyPlaybackState?> = _playbackState.asStateFlow()
    companion object {private const val TAG = "SpotifyRepository"}
    private var accessToken: String? = null

    override fun setAccessToken(token: String) {
        accessToken = token
        Logx.storage(TAG, "Token set: ${token.take(10)}...")
    }

    private fun getAuthHeader(): String = "Bearer $accessToken"

    override suspend fun getCurrentPlayback(): Result<SpotifyPlaybackState?> {
        return try {
            if (accessToken == null) {
                Logx.error(TAG, "No access token available")
                return Result.failure(Exception("No access token"))
            }

            Logx.network(TAG, "Getting current playback…")
            val response = spotifyApi.getCurrentPlayback(getAuthHeader())

            if (response.isSuccessful) {
                val playbackState = response.body()
                _playbackState.value = playbackState
                Logx.info(TAG, "Playback state updated: ${playbackState?.isPlaying}")
                Result.success(playbackState)
            } else {
                Logx.error(TAG, "Failed to get playback state: ${response.code()} - ${response.message()}")
                Result.failure(Exception("Failed to get playback state: ${response.code()}"))
            }
        } catch (e: Exception) {
            Logx.error(TAG, "Exception getting playback state", e)
            Result.failure(e)
        }
    }

    override suspend fun play(): Result<Unit> {
        return try {
            if (accessToken == null) {
                return Result.failure(Exception("No access token"))
            }

            Logx.network(TAG, "Playing…")
            val response = spotifyApi.resumePlayback(getAuthHeader())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Logx.error(TAG, "Failed to play: ${response.code()}")
                Result.failure(Exception("Failed to play: ${response.code()}"))
            }
        } catch (e: Exception) {
            Logx.error(TAG, "Exception playing", e)
            Result.failure(e)
        }
    }

    override suspend fun pause(): Result<Unit> {
        return try {
            if (accessToken == null) {
                return Result.failure(Exception("No access token"))
            }

            Logx.network(TAG, "Pausing…")
            val response = spotifyApi.pausePlayback(getAuthHeader())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Logx.error(TAG, "Failed to pause: ${response.code()}")
                Result.failure(Exception("Failed to pause: ${response.code()}"))
            }
        } catch (e: Exception) {
            Logx.error(TAG, "Exception pausing", e)
            Result.failure(e)
        }
    }

    override suspend fun next(): Result<Unit> {
        return try {
            if (accessToken == null) {
                return Result.failure(Exception("No access token"))
            }

            Logx.network(TAG, "Skipping to next…")
            val response = spotifyApi.skipToNext(getAuthHeader())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Logx.error(TAG, "Failed to skip next: ${response.code()}")
                Result.failure(Exception("Failed to skip next: ${response.code()}"))
            }
        } catch (e: Exception) {
            Logx.network(TAG, "Skipping to next failed")
            Result.failure(e)
        }
    }

    override suspend fun previous(): Result<Unit> {
        return try {
            if (accessToken == null) {
                return Result.failure(Exception("No access token"))
            }
            Log.d("SpotifyRepository", "Skipping to previous...")
            val response = spotifyApi.skipToPrevious(getAuthHeader())
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Logx.error(TAG, "Failed to skip previous: ${response.code()}")
                Result.failure(Exception("Failed to skip previous: ${response.code()}"))
            }
        } catch (e: Exception) {
            Logx.error(TAG, "Exception skipping previous", e)
            Result.failure(e)
        }
    }

    override suspend fun seekTo(positionMs: Long): Result<Unit> {
        return try {
            if (accessToken == null) {
                return Result.failure(Exception("No access token"))
            }

            Logx.network(TAG, "Seeking to $positionMs…")
            val response = spotifyApi.seekToPosition(getAuthHeader(), positionMs)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Logx.error(TAG, "Failed to seek: ${response.code()}")
                Result.failure(Exception("Failed to seek: ${response.code()}"))
            }
        } catch (e: Exception) {
            Logx.error(TAG, "Exception seeking", e)
            Result.failure(e)
        }
    }
}