package com.leoevg.maftimer.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationPaths {
    @Serializable object MainScreenSealed: NavigationPaths
    @Serializable object SettingsScreenSealed: NavigationPaths
    @Serializable object LocalSongsScreenSealed: NavigationPaths
}