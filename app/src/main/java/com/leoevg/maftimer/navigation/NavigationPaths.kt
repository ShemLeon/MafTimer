package com.leoevg.maftimer.navigation

import kotlinx.serialization.Serializable

interface NavigationPaths {
    @Serializable data object TimerSealed: NavigationPaths
    @Serializable data object SettingsSealed: NavigationPaths
}