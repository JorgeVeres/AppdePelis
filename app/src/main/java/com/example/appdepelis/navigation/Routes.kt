package com.example.appdepelis.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Home : Routes("home")
    object Detail : Routes("detail/{movieId}") {
        fun createRoute(movieId: Int) = "detail/$movieId"
    }
    object Notes : Routes("notes/{movieId}") {
        fun createRoute(movieId: Int) = "notes/$movieId"
    }
    object Settings : Routes("settings")
}