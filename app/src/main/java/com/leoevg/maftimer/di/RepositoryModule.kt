package com.leoevg.maftimer.di

import com.leoevg.maftimer.domain.repository.ISpotifyRepository
import com.leoevg.maftimer.data.repository.SpotifyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSpotifyRepository(impl: SpotifyRepository): ISpotifyRepository
}