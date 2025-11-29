package com.example.appdepelis.di

import android.content.Context
import com.example.appdepelis.data.local.MovieDatabase
import com.example.appdepelis.data.prefs.UserPrefsRepository
import com.example.appdepelis.data.repository.MovieRepository

object ServiceLocator {

    private var movieDatabase: MovieDatabase? = null
    private var movieRepository: MovieRepository? = null
    private var userPrefsRepository: UserPrefsRepository? = null

    fun provideMovieRepository(context: Context): MovieRepository {
        return movieRepository ?: synchronized(this) {
            val database = provideMovieDatabase(context)
            val repo = MovieRepository(database.movieDao())
            movieRepository = repo
            repo
        }
    }

    fun provideUserPrefsRepository(context: Context): UserPrefsRepository {
        return userPrefsRepository ?: synchronized(this) {
            val repo = UserPrefsRepository(context)
            userPrefsRepository = repo
            repo
        }
    }

    private fun provideMovieDatabase(context: Context): MovieDatabase {
        return movieDatabase ?: synchronized(this) {
            val db = MovieDatabase.getDatabase(context)
            movieDatabase = db
            db
        }
    }

    fun resetRepositories() {
        movieRepository = null
        userPrefsRepository = null
        movieDatabase = null
    }
}