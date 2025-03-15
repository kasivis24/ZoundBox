package com.mobile.apicalljetcompose.ui.theme.route

import PlayerViewModel
import SearchScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mobile.apicalljetcompose.ui.theme.constants.AppConstants
import com.mobile.apicalljetcompose.ui.theme.screens.PlayerScreen
import com.mobile.apicalljetcompose.ui.theme.screens.PlaylistSongsScreen
import com.mobile.apicalljetcompose.ui.theme.screens.SplashScreen
import com.mobile.apicalljetcompose.ui.theme.screens.TabScreen
import com.mobile.apicalljetcompose.ui.theme.thememanager.ThemePreferenceManager
import com.mobile.apicalljetcompose.ui.theme.viewmodel.SongViewModel

@Composable
fun AppNavigation(isDark : Boolean,onChange : () -> Unit,playerViewModel: PlayerViewModel,songViewModel: SongViewModel,navHostController: NavHostController,themePreferenceManager: ThemePreferenceManager){
    NavHost(navController = navHostController, startDestination = AppConstants.SPLASH_SCREEN_ROUTE){
        composable(AppConstants.SPLASH_SCREEN_ROUTE) { SplashScreen(navController = navHostController) }
        composable(AppConstants.TAB_SCREEN_ROUTE){ TabScreen(isDark,onChange, playerViewModel,songViewModel,navController = navHostController,themePreferenceManager) }
        composable(AppConstants.PLAYER_SCREEN_ROUTE){ PlayerScreen(isDark,onChange, playerViewModel,navController = navHostController) }
        composable(AppConstants.SEARCH_SCREEN_ROUTE){ SearchScreen(isDark,onChange, playerViewModel,songViewModel,navController = navHostController) }
        composable("${AppConstants.PLAYLIST_SCREEN_ROUTE}/{playlistId}/{playlistName}",
            arguments = listOf(
                navArgument("playlistId") { type = NavType.LongType },
                navArgument("playlistName") { type = NavType.StringType },
                )
            ){ PlaylistSongsScreen(isDark,onChange, playlistId = it.arguments?.getLong("playlistId") ?: 0L, playlistName = it.arguments?.getString("playlistName") ?: "Unknown", playerViewModel,songViewModel,navController = navHostController) } }
}