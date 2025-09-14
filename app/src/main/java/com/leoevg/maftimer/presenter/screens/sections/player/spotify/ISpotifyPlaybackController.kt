package com.leoevg.maftimer.presenter.screens.sections.player.spotify

interface ISpotifyPlaybackController {
    fun isAuthorized(): Boolean
    fun setAccessFromStored()
    suspend fun play()
    suspend fun pause()
    suspend fun next()
    suspend fun previous()
    suspend fun seekTo(positionMs: Long)
    suspend fun getPlayback(): Result<RemotePlayback?>
    fun clearAuthOn401()
    fun openSpotifyApp()
}