package com.example.appdepelis.data.mappers

import com.example.appdepelis.data.local.entity.MovieEntity
import com.example.appdepelis.data.model.Movie

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        director = director,
        year = year,
        genre = genre,
        rating = rating,
        synopsis = synopsis,
        posterUrl = posterUrl,
        isFavorite = isFavorite,
        watchedDate = watchedDate,
        personalNotes = personalNotes
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        director = director,
        year = year,
        genre = genre,
        rating = rating,
        synopsis = synopsis,
        posterUrl = posterUrl,
        isFavorite = isFavorite,
        watchedDate = watchedDate,
        personalNotes = personalNotes
    )
}

fun List<MovieEntity>.toMovieList(): List<Movie> {
    return map { it.toMovie() }
}