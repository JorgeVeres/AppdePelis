package com.example.appdepelis.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdepelis.data.model.Movie
import com.example.appdepelis.data.prefs.UserPrefsRepository
import com.example.appdepelis.data.repository.MovieRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MoviesUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val showFavoritesOnly: Boolean = false,
    val sortOrder: String = "title"
)

class MoviesViewModel(
    private val movieRepository: MovieRepository,
    private val prefsRepository: UserPrefsRepository // Lo mantenemos para las preferencias de UI
) : ViewModel() {


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Conectamos directamente las preferencias de la UI a StateFlows para que el ViewModel reaccione
    private val _showFavoritesOnly: StateFlow<Boolean> = prefsRepository.showFavoritesOnly
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _sortOrder: StateFlow<String> = prefsRepository.sortOrder
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "title")

    // Usamos el flujo de películas del repositorio, que ya no filtra por usuario
    private val moviesFlow = movieRepository.movies

    val uiState: StateFlow<MoviesUiState> = combine(
        moviesFlow,
        _searchQuery,
        _showFavoritesOnly,
        _sortOrder
    ) { movies, query, favoritesOnly, sortOrder ->

        // 1. Filtrar por favoritos
        val favoritesFilteredMovies = if (favoritesOnly) {
            movies.filter { it.isFavorite }
        } else {
            movies
        }

        // 2. Filtrar por búsqueda
        val searchFilteredMovies = if (query.isNotBlank()) {
            favoritesFilteredMovies.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.director.contains(query, ignoreCase = true)
            }
        } else {
            favoritesFilteredMovies
        }

        // 3. Ordenar
        val sortedMovies = when (sortOrder) {
            "title" -> searchFilteredMovies.sortedBy { it.title }
            "year" -> searchFilteredMovies.sortedByDescending { it.year }
            "rating" -> searchFilteredMovies.sortedByDescending { it.rating }

            "date" -> searchFilteredMovies.sortedByDescending { it.watchedDate ?: 0L }
            else -> searchFilteredMovies
        }

        MoviesUiState(
            movies = sortedMovies,
            isLoading = false,
            searchQuery = query,
            showFavoritesOnly = favoritesOnly,
            sortOrder = sortOrder
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MoviesUiState(isLoading = true)
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavoritesFilter() {
        viewModelScope.launch {
            // Obtenemos el valor actual y lo negamos para guardarlo
            val newValue = !_showFavoritesOnly.value
            prefsRepository.setShowFavoritesOnly(newValue)
        }
    }

    fun setSortOrder(order: String) {
        viewModelScope.launch {
            prefsRepository.setSortOrder(order)
        }
    }

    // Estas funciones llaman al repositorio simple, que ya no necesita el `username`
    fun insertMovie(movie: Movie) {
        viewModelScope.launch {
            movieRepository.insertMovie(movie)
        }
    }

    fun updateMovie(movie: Movie) {
        viewModelScope.launch {
            movieRepository.updateMovie(movie)
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            movieRepository.deleteMovie(movie)
        }
    }

    fun toggleFavorite(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            movieRepository.toggleFavorite(movieId, !isFavorite)
        }
    }

    suspend fun getMovieById(id: Int): Movie? {
        return movieRepository.getMovieById(id)
    }
}