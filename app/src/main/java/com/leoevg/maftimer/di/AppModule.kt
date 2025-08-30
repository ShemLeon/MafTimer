package com.leoevg.maftimer.di

import com.leoevg.maftimer.data.SpotifyAuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSpotifyAuthManager(): SpotifyAuthManager {
        return SpotifyAuthManager()
    }
}