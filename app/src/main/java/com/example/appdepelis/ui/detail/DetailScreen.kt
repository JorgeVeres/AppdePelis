package com.example.appdepelis.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appdepelis.data.model.Movie

import com.example.appdepelis.ui.common.MovieFormats
import com.example.appdepelis.ui.common.MovieValidation
import com.example.appdepelis.ui.detail.components.MovieDetailCard
import com.example.appdepelis.ui.movies.MoviesViewModel
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movieId: Int,
    viewModel: MoviesViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToNotes: (Int) -> Unit
) {
    var movie by remember { mutableStateOf<Movie?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(movieId) {
        movie = viewModel.getMovieById(movieId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Película") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    movie?.let { currentMovie ->
                        IconButton(
                            onClick = {
                                viewModel.toggleFavorite(currentMovie.id, currentMovie.isFavorite)
                                movie = currentMovie.copy(isFavorite = !currentMovie.isFavorite)
                            }
                        ) {
                            Icon(
                                imageVector = if (currentMovie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (currentMovie.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }

                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        movie?.let { currentMovie ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MovieDetailCard(movie = currentMovie)

                if (currentMovie.synopsis.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Sinopsis",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = currentMovie.synopsis,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                if (currentMovie.personalNotes.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Mis Notas",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Icon(
                                    imageVector = Icons.Default.Note,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = currentMovie.personalNotes,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        scope.launch {
                            val updatedMovie = currentMovie.copy(

                                watchedDate = if (currentMovie.watchedDate == null) {
                                    MovieFormats.getCurrentTimestamp()
                                } else null
                            )
                            viewModel.updateMovie(updatedMovie)
                            movie = updatedMovie
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (currentMovie.watchedDate != null) Icons.Default.CheckCircle else Icons.Default.Circle,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (currentMovie.watchedDate != null) "Marcar como no vista" else "Marcar como vista"
                    )
                }
            }

            if (showEditDialog) {
                EditMovieDialog(
                    movie = currentMovie,
                    onDismiss = { showEditDialog = false },
                    onConfirm = { updatedMovie ->
                        viewModel.updateMovie(updatedMovie)
                        movie = updatedMovie
                        showEditDialog = false
                    }
                )
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    icon = { Icon(Icons.Default.Delete, contentDescription = null) },
                    title = { Text("Eliminar película") },
                    text = { Text("¿Estás seguro de que quieres eliminar \"${currentMovie.title}\"?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.deleteMovie(currentMovie)
                                onNavigateBack()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMovieDialog(
    movie: Movie,
    onDismiss: () -> Unit,
    onConfirm: (Movie) -> Unit
) {
    var title by remember { mutableStateOf(movie.title) }
    var director by remember { mutableStateOf(movie.director) }
    var year by remember { mutableStateOf(movie.year.toString()) }
    var selectedGenre by remember { mutableStateOf(movie.genre) }
    var rating by remember { mutableStateOf(movie.rating.toString()) }
    var synopsis by remember { mutableStateOf(movie.synopsis) }
    var personalNotes by remember { mutableStateOf(movie.personalNotes) }
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Película") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = director,
                    onValueChange = { director = it },
                    label = { Text("Director") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = year,
                    onValueChange = { if (it.length <= 4) year = it },
                    label = { Text("Año") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedGenre,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Género") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        MovieFormats.getGenresList().forEach { genre ->
                            DropdownMenuItem(
                                text = { Text("${MovieFormats.getGenreEmoji(genre)} $genre") },
                                onClick = {
                                    selectedGenre = genre
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = rating,
                    onValueChange = { if (it.toFloatOrNull() != null || it.isEmpty()) rating = it },
                    label = { Text("Valoración (0-10)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = synopsis,
                    onValueChange = { synopsis = it },
                    label = { Text("Sinopsis") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                OutlinedTextField(
                    value = personalNotes,
                    onValueChange = { personalNotes = it },
                    label = { Text("Notas personales") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val yearInt = year.toIntOrNull() ?: 0
                    val ratingFloat = rating.toFloatOrNull() ?: 0f

                    val errors = MovieValidation.validateMovie(
                        title = title,
                        director = director,
                        year = yearInt,
                        genre = selectedGenre,
                        rating = ratingFloat,
                        synopsis = synopsis
                    )

                    if (errors.isEmpty()) {
                        val updatedMovie = movie.copy(
                            title = title.trim(),
                            director = director.trim(),
                            year = yearInt,
                            genre = selectedGenre,
                            rating = ratingFloat,
                            synopsis = synopsis.trim(),
                            personalNotes = personalNotes.trim()
                        )
                        onConfirm(updatedMovie)
                    } else {
                        errorMessage = errors.first()
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
