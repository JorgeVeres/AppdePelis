package com.example.appdepelis.data.repository

import com.example.appdepelis.data.local.dao.MovieDao
import com.example.appdepelis.data.mappers.toEntity
import com.example.appdepelis.data.mappers.toMovie
import com.example.appdepelis.data.mappers.toMovieList
import com.example.appdepelis.data.model.Movie
// import com.example.appdepelis.data.prefs.UserPrefsRepository // <-- YA NO SE NECESITA
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Quitamos userPrefsRepository del constructor
class MovieRepository(private val movieDao: MovieDao) {

    // Volvemos a los flujos simples que no dependen del usuario
    val movies: Flow<List<Movie>> = movieDao.getAllMovies().map { entities ->
        entities.toMovieList()
    }

    val favoriteMovies: Flow<List<Movie>> = movieDao.getFavoriteMovies().map { entities ->
        entities.toMovieList()
    }

    // Ya no se filtra por usuario
    suspend fun getMovieById(id: Int): Movie? {
        return movieDao.getMovieById(id)?.toMovie()
    }

    // La búsqueda ya no depende del usuario
    fun searchMovies(query: String): Flow<List<Movie>> {
        return movieDao.searchMovies(query).map { entities ->
            entities.toMovieList()
        }
    }

    // Ya no se asigna el username
    suspend fun insertMovie(movie: Movie): Long {
        return movieDao.insertMovie(movie.toEntity())
    }

    // Ya no se asigna el username
    suspend fun updateMovie(movie: Movie) {
        movieDao.updateMovie(movie.toEntity())
    }

    suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteMovie(movie.toEntity())
    }

    suspend fun deleteMovieById(id: Int) {
        movieDao.deleteMovieById(id)
    }

    suspend fun toggleFavorite(movieId: Int, isFavorite: Boolean) {
        movieDao.updateFavoriteStatus(movieId, isFavorite)
    }
}