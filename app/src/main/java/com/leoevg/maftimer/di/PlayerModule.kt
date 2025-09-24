package com.leoevg.maftimer.di

import com.leoevg.maftimer.presentation.screens.sections.player.local.ILocalPlayerController
import com.leoevg.maftimer.presentation.screens.sections.player.spotify.ISpotifyPlaybackController
import com.leoevg.maftimer.presentation.screens.sections.player.local.LocalPlayerControllerImpl
import com.leoevg.maftimer.presentation.screens.sections.player.spotify.SpotifyPlaybackControllerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {
    @Provides @Singleton
    fun provideLocalPlayerController(impl: LocalPlayerControllerImpl): ILocalPlayerController = impl

    @Provides @Singleton
    fun provideSpotifyController(impl: SpotifyPlaybackControllerImpl): ISpotifyPlaybackController = impl
}