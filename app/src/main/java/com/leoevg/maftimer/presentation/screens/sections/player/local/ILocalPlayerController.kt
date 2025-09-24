package com.leoevg.maftimer.presentation.screens.sections.player.local

import kotlinx.coroutines.flow.StateFlow

interface ILocalPlayerController  {
    val state: StateFlow<LocalPlayback>
    suspend fun initLibrary()
    fun play()
    fun pause()
    fun next()
    fun previous()
    fun seekTo(positionMs: Long)
    fun refresh()
    fun release()
}