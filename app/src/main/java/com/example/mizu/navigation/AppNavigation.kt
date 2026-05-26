package com.example.mizu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mizu.features.dashboard.Dashboard
import com.example.mizu.features.history.History
import com.example.mizu.features.settings.Settings

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier) {
    NavHost(navController = navController, startDestination = Screen.Dashboard.route, modifier = modifier) {
        composable(Screen.Dashboard.route) { Dashboard(modifier = modifier) }
        composable(Screen.History.route) { History(modifier = modifier) }
        composable(Screen.Settings.route) { Settings(modifier = modifier) }
    }
}