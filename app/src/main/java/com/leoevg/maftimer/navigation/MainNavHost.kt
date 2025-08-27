package com.leoevg.maftimer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leoevg.maftimer.presenter.screens.settings.SettingsScreen
import com.leoevg.maftimer.presenter.screens.main.MainScreen

@Composable
fun MainNavHost(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationPaths.TimerSealed
    ){
        composable<NavigationPaths.TimerSealed> {
            MainScreen { path ->
                navController.navigate(path)
            }
        }

        composable<NavigationPaths.SettingsSealed> {
                SettingsScreen()
            }
        }
    }

