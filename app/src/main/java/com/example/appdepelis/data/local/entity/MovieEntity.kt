package com.example.appdepelis.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val director: String,
    val year: Int,
    val genre: String,
    val rating: Float,
    val synopsis: String,
    val posterUrl: String,
    val isFavorite: Boolean,
    val watchedDate: Long?,
    val personalNotes: String
)