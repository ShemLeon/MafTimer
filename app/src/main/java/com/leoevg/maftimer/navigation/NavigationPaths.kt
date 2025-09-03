package com.leoevg.maftimer.navigation

import kotlinx.serialization.Serializable

sealed interface NavigationPaths {
    @Serializable object MainScreenSealed: NavigationPaths
    @Serializable object SettingsScreenSealed: NavigationPaths
}