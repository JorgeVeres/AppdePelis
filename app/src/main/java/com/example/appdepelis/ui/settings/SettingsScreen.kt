package com.example.appdepelis.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.appdepelis.di.ServiceLocator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val prefsRepository = remember { ServiceLocator.provideUserPrefsRepository(context) }
    val scope = rememberCoroutineScope()

    val username by prefsRepository.username.collectAsState(initial = "")
    val themeMode by prefsRepository.themeMode.collectAsState(initial = "system")
    val sortOrder by prefsRepository.sortOrder.collectAsState(initial = "title")
    val showFavoritesOnly by prefsRepository.showFavoritesOnly.collectAsState(initial = false)

    var showThemeDialog by remember { mutableStateOf(false) }
    var showSortDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Sección de usuario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Usuario",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = username.ifEmpty { "Invitado" },
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }

            // Sección de apariencia
            SettingsSection(title = "Apariencia")

            SettingsItem(
                icon = Icons.Default.Palette,
                title = "Tema",
                subtitle = when (themeMode) {
                    "light" -> "Claro"
                    "dark" -> "Oscuro"
                    else -> "Sistema"
                },
                onClick = { showThemeDialog = true }
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            // Sección de películas
            SettingsSection(title = "Películas")

            SettingsItem(
                icon = Icons.Default.Sort,
                title = "Orden predeterminado",
                subtitle = when (sortOrder) {
                    "title" -> "Por título"
                    "year" -> "Por año"
                    "rating" -> "Por valoración"
                    "date" -> "Por fecha de visionado"
                    else -> "Por título"
                },
                onClick = { showSortDialog = true }
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            SettingsSwitchItem(
                icon = Icons.Default.Favorite,
                title = "Mostrar solo favoritos",
                subtitle = "Filtrar automáticamente por favoritos al iniciar",
                checked = showFavoritesOnly,
                onCheckedChange = {
                    scope.launch {
                        prefsRepository.setShowFavoritesOnly(it)
                    }
                }
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            // Sección de información
            SettingsSection(title = "Información")

            SettingsItem(
                icon = Icons.Default.Info,
                title = "Versión",
                subtitle = "1.0.0",
                onClick = { }
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            SettingsItem(
                icon = Icons.Default.Code,
                title = "Acerca de",
                subtitle = "App de gestión de películas con Jetpack Compose",
                onClick = { }
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            // Botón de cerrar sesión
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Diálogo de tema
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Seleccionar tema") },
            text = {
                Column {
                    ThemeOption("Sistema", "system", themeMode) {
                        scope.launch {
                            prefsRepository.setThemeMode("system")
                            showThemeDialog = false
                        }
                    }
                    ThemeOption("Claro", "light", themeMode) {
                        scope.launch {
                            prefsRepository.setThemeMode("light")
                            showThemeDialog = false
                        }
                    }
                    ThemeOption("Oscuro", "dark", themeMode) {
                        scope.launch {
                            prefsRepository.setThemeMode("dark")
                            showThemeDialog = false
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // Diálogo de orden
    if (showSortDialog) {
        AlertDialog(
            onDismissRequest = { showSortDialog = false },
            title = { Text("Ordenar por") },
            text = {
                Column {
                    SortOption("Por título", "title", sortOrder) {
                        scope.launch {
                            prefsRepository.setSortOrder("title")
                            showSortDialog = false
                        }
                    }
                    SortOption("Por año", "year", sortOrder) {
                        scope.launch {
                            prefsRepository.setSortOrder("year")
                            showSortDialog = false
                        }
                    }
                    SortOption("Por valoración", "rating", sortOrder) {
                        scope.launch {
                            prefsRepository.setSortOrder("rating")
                            showSortDialog = false
                        }
                    }
                    SortOption("Por fecha de visionado", "date", sortOrder) {
                        scope.launch {
                            prefsRepository.setSortOrder("date")
                            showSortDialog = false
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSortDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // Diálogo de cerrar sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = { Icon(Icons.Default.Logout, contentDescription = null) },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Estás seguro de que quieres cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            prefsRepository.logout()
                            // En producción, navegar a login
                            showLogoutDialog = false
                        }
                    }
                ) {
                    Text("Cerrar sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun SettingsSection(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SettingsSwitchItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun ThemeOption(
    label: String,
    value: String,
    currentValue: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = value == currentValue,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}

@Composable
fun SortOption(
    label: String,
    value: String,
    currentValue: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = value == currentValue,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}