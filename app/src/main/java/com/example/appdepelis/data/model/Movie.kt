package com.example.appdepelis.data.model

data class Movie(
    val id: Int = 0,
    val title: String,
    val director: String,
    val year: Int,
    val genre: String,
    val rating: Float = 0f,
    val synopsis: String = "",
    val posterUrl: String = "",
    val isFavorite: Boolean = false,
    val watchedDate: Long? = null,
    val personalNotes: String = ""
)