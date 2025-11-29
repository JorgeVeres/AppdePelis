package com.example.appdepelis

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController()
): AppState {
    return remember(navController) {
        AppState(navController)
    }
}

class AppState(
    val navController: NavHostController
) {
    val currentRoute: String?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination?.route

    val shouldShowBottomBar: Boolean
        @Composable get() {
            val route = currentRoute
            return route != null &&
                    route != "login" &&
                    !route.startsWith("detail/") &&
                    !route.startsWith("notes/")
        }

    fun navigateToHome() {
        navController.navigate("home") {
            popUpTo("home") { inclusive = true }
        }
    }

    fun navigateToSettings() {
        navController.navigate("settings")
    }

    fun navigateToDetail(movieId: Int) {
        navController.navigate("detail/$movieId")
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}