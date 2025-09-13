package com.leoevg.maftimer.domain.repository

import com.leoevg.maftimer.data.api.SpotifyPlaybackState

interface ISpotifyRepository {
    suspend fun play(): Result<Unit>            //
    suspend fun pause(): Result<Unit>
    suspend fun next(): Result<Unit>
    suspend fun previous(): Result<Unit>
    suspend fun seekTo(positionMs: Long): Result<Unit>
    suspend fun getCurrentPlayback(): Result<SpotifyPlaybackState?>
    fun setAccessToken(token: String)
}