package com.leoevg.maftimer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leoevg.maftimer.presentation.screens.settings.SettingsScreen
import com.leoevg.maftimer.presentation.screens.main.MainScreen
import com.leoevg.maftimer.presentation.screens.sections.player.local.LocalSongsScreen

@Composable
fun MainNavHost(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationPaths.MainScreenSealed
    ){
        composable<NavigationPaths.MainScreenSealed> {
            MainScreen(
                navigate = { path ->
                    navController.navigate(path)
                }
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
                onPick = { _, _ ->  navController.popBackStack()   }
            )
        }
    }
}