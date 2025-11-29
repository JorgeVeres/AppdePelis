package com.example.appdepelis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.appdepelis.di.ServiceLocator
import com.example.appdepelis.navigation.NavGraph
import com.example.appdepelis.navigation.Routes
import com.example.appdepelis.theme.AppdePelisTheme
import com.example.appdepelis.ui.movies.MoviesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppdePelisTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieApp()
                }
            }
        }
    }
}

@Composable
fun MovieApp() {
    val context = LocalContext.current
    val appState = rememberAppState()

    // Inicializar repositorios
    val movieRepository = remember { ServiceLocator.provideMovieRepository(context) }
    val prefsRepository = remember { ServiceLocator.provideUserPrefsRepository(context) }

    // Crear ViewModel
    val moviesViewModel = remember {
        MoviesViewModel(movieRepository, prefsRepository)
    }

    // Verificar si el usuario está logueado
    val isLoggedIn by prefsRepository.isLoggedIn.collectAsState(initial = false)

    val startDestination = if (isLoggedIn) Routes.Home.route else Routes.Login.route

    NavGraph(
        navController = appState.navController,
        startDestination = startDestination,
        moviesViewModel = moviesViewModel
    )
}