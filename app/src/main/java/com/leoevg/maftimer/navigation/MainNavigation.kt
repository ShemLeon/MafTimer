package com.leoevg.maftimer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leoevg.maftimer.screens.settings.SettingsScreen
import com.leoevg.maftimer.screens.timer.TimerScreen

@Composable
fun MainNavHost(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationPaths.TimerSealed
    ){
        composable<NavigationPaths.TimerSealed> {
            TimerScreen { path ->
                navController.navigate(path)
            }
        }
        composable<NavigationPaths.SettingsSealed> {
                SettingsScreen()
            }
        }
    }

