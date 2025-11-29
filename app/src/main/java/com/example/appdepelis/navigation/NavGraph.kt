package com.example.appdepelis.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.appdepelis.ui.detail.DetailScreen
import com.example.appdepelis.ui.home.HomeScreen
import com.example.appdepelis.ui.login.LoginScreen
import com.example.appdepelis.ui.movies.MoviesViewModel
import com.example.appdepelis.ui.settings.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    moviesViewModel: MoviesViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Home.route) {
            HomeScreen(
                viewModel = moviesViewModel,
                onMovieClick = { movieId ->
                    navController.navigate(Routes.Detail.createRoute(movieId))
                },
                onSettingsClick = {
                    navController.navigate(Routes.Settings.route)
                }
            )
        }

        composable(
            route = Routes.Detail.route,
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
            DetailScreen(
                movieId = movieId,
                viewModel = moviesViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToNotes = { id ->
                    navController.navigate(Routes.Notes.createRoute(id))
                }
            )
        }

        composable(
            route = Routes.Notes.route,
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
            // NotesScreen aquÃ si la implementas
            // Por ahora redirige al detalle
        }

        composable(Routes.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}