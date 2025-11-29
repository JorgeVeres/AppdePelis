package com.example.appdepelis.ui.common

import java.text.SimpleDateFormat
import java.util.*

object MovieFormats {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun formatDate(timestamp: Long?): String {
        return if (timestamp != null) {
            dateFormat.format(Date(timestamp))
        } else {
            "Sin fecha"
        }
    }

    fun formatDateTime(timestamp: Long?): String {
        return if (timestamp != null) {
            dateTimeFormat.format(Date(timestamp))
        } else {
            "Sin fecha"
        }
    }

    fun formatRating(rating: Float): String {
        return String.format("%.1f", rating)
    }

    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }

    fun getGenreEmoji(genre: String): String {
        return when (genre.lowercase()) {
            "acción", "accion" -> "💥"
            "comedia" -> "😂"
            "drama" -> "🎭"
            "terror", "horror" -> "👻"
            "ciencia ficción", "ciencia ficcion", "sci-fi" -> "🚀"
            "romance" -> "❤️"
            "thriller" -> "🔪"
            "aventura" -> "🗺️"
            "fantasía", "fantasia" -> "🧙"
            "animación", "animacion" -> "🎨"
            "documental" -> "📹"
            "musical" -> "🎵"
            "western" -> "🤠"
            "crimen" -> "🕵️"
            "misterio" -> "🔍"
            else -> "🎬"
        }
    }

    fun getYearRange(): List<Int> {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return (1900..currentYear).toList().reversed()
    }

    fun getGenresList(): List<String> {
        return listOf(
            "Acción",
            "Comedia",
            "Drama",
            "Terror",
            "Ciencia Ficción",
            "Romance",
            "Thriller",
            "Aventura",
            "Fantasía",
            "Animación",
            "Documental",
            "Musical",
            "Western",
            "Crimen",
            "Misterio"
        )
    }
}