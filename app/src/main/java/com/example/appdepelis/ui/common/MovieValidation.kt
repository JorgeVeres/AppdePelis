package com.example.appdepelis.ui.common

object MovieValidation {

    data class ValidationResult(
        val isValid: Boolean,
        val errorMessage: String? = null
    )

    fun validateTitle(title: String): ValidationResult {
        return when {
            title.isBlank() -> ValidationResult(false, "El título no puede estar vacío")
            title.length < 2 -> ValidationResult(false, "El título debe tener al menos 2 caracteres")
            title.length > 100 -> ValidationResult(false, "El título es demasiado largo (máx. 100 caracteres)")
            else -> ValidationResult(true)
        }
    }

    fun validateDirector(director: String): ValidationResult {
        return when {
            director.isBlank() -> ValidationResult(false, "El director no puede estar vacío")
            director.length < 2 -> ValidationResult(false, "El nombre del director debe tener al menos 2 caracteres")
            director.length > 50 -> ValidationResult(false, "El nombre es demasiado largo (máx. 50 caracteres)")
            else -> ValidationResult(true)
        }
    }

    fun validateYear(year: Int): ValidationResult {
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        return when {
            year < 1888 -> ValidationResult(false, "El año no puede ser anterior a 1888 (primera película)")
            year > currentYear + 5 -> ValidationResult(false, "El año no puede ser tan futuro")
            else -> ValidationResult(true)
        }
    }

    fun validateRating(rating: Float): ValidationResult {
        return when {
            rating < 0f -> ValidationResult(false, "La valoración no puede ser negativa")
            rating > 10f -> ValidationResult(false, "La valoración no puede ser mayor a 10")
            else -> ValidationResult(true)
        }
    }

    fun validateGenre(genre: String): ValidationResult {
        return when {
            genre.isBlank() -> ValidationResult(false, "Debe seleccionar un género")
            else -> ValidationResult(true)
        }
    }

    fun validateSynopsis(synopsis: String): ValidationResult {
        return when {
            synopsis.length > 1000 -> ValidationResult(false, "La sinopsis es demasiado larga (máx. 1000 caracteres)")
            else -> ValidationResult(true)
        }
    }

    fun validateUrl(url: String): ValidationResult {
        if (url.isBlank()) return ValidationResult(true) // URL es opcional

        val urlPattern = Regex(
            "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$",
            RegexOption.IGNORE_CASE
        )

        return when {
            !url.matches(urlPattern) -> ValidationResult(false, "URL no válida")
            else -> ValidationResult(true)
        }
    }

    fun validateMovie(
        title: String,
        director: String,
        year: Int,
        genre: String,
        rating: Float,
        synopsis: String = "",
        posterUrl: String = ""
    ): List<String> {
        val errors = mutableListOf<String>()

        validateTitle(title).errorMessage?.let { errors.add(it) }
        validateDirector(director).errorMessage?.let { errors.add(it) }
        validateYear(year).errorMessage?.let { errors.add(it) }
        validateGenre(genre).errorMessage?.let { errors.add(it) }
        validateRating(rating).errorMessage?.let { errors.add(it) }
        validateSynopsis(synopsis).errorMessage?.let { errors.add(it) }
        validateUrl(posterUrl).errorMessage?.let { errors.add(it) }

        return errors
    }
}