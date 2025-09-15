package com.leoevg.maftimer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leoevg.maftimer.presenter.screens.settings.SettingsScreen
import com.leoevg.maftimer.presenter.screens.main.MainScreen
import com.leoevg.maftimer.presenter.screens.sections.player.local.LocalSongsScreen

@Composable
fun MainNavHost(
    onSpotifyAuthRequest: () -> Unit = {},
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationPaths.MainScreenSealed
    ){
        composable<NavigationPaths.MainScreenSealed> {
            MainScreen(
                navigate = { path ->
                    navController.navigate(path)
                           },
                onSpotifyAuthRequest = onSpotifyAuthRequest
            )
        }

        composable<NavigationPaths.SettingsScreenSealed> {
            SettingsScreen(
                onNavigateHome = {
                    navController.popBackStack()
                }
            )
        }

        composable<NavigationPaths.LocalSongsScreenSealed> {
            LocalSongsScreen(
                onBack = { navController.popBackStack() },
                onPick = { _, _ ->
                    // For now just close the list; hook playback in the next step
                    navController.popBackStack()
                }
            )
        }
    }
}