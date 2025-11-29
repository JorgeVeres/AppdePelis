package com.example.appdepelis.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appdepelis.di.ServiceLocator
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val prefsRepository = remember { ServiceLocator.provideUserPrefsRepository(context) }
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = "App Icon",
                    modifier = Modifier.size(72.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Mis Películas",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Gestiona tu colección de películas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        errorMessage = null
                    },
                    label = { Text("Usuario") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = "Usuario")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = errorMessage != null
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text("Contraseña") },
                    leadingIcon = {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Visibilidad"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = errorMessage != null
                )

                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Button(
                    onClick = {
                        when {
                            username.isBlank() -> {
                                errorMessage = "El usuario no puede estar vacío"
                            }
                            password.isBlank() -> {
                                errorMessage = "La contraseña no puede estar vacía"
                            }
                            password.length < 4 -> {
                                errorMessage = "La contraseña debe tener al menos 4 caracteres"
                            }
                            else -> {
                                isLoading = true
                                scope.launch {
                                    // Simular login (en producción verificarías con API)
                                    kotlinx.coroutines.delay(500)
                                    prefsRepository.setLoginStatus(true, username)
                                    onLoginSuccess()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Iniciar Sesión")
                    }
                }

                TextButton(
                    onClick = {
                        // Demo login rápido
                        scope.launch {
                            prefsRepository.setLoginStatus(true, "Demo User")
                            onLoginSuccess()
                        }
                    }
                ) {
                    Text("Entrar como invitado")
                }
            }
        }
    }
}