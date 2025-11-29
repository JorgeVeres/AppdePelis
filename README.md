
# 🎬 Mis Películas

**Proyecto Final UT2 - App Compose con Persistencia**

Aplicación Android para gestionar una colección de películas con persistencia local (Room + DataStore) y arquitectura MVVM.

---

## 📱 ¿Qué hace la app?

- ✅ Crear, editar, ver y eliminar películas
- 🔍 Buscar en tiempo real (título, director, género)
- ⭐ Sistema de favoritos con filtrado
- 📊 Ordenar por título, año, valoración o fecha
- ✅ Marcar películas como vistas
- 📝 Añadir notas personales
- 🌓 Modo oscuro/claro
- 💾 Persistencia completa de datos

---

## 🚀 Cómo ejecutar

### Requisitos
- Android Studio Hedgehog+
- JDK 17
- Android SDK 34
- Dispositivo/Emulador API 26+

### Pasos
```bash
# 1. Clonar repositorio
git clone https://github.com/tu-usuario/app-de-pelis.git

# 2. Abrir en Android Studio y sincronizar Gradle

# 3. Ejecutar (Shift + F10)

# 4. Login: cualquier usuario/contraseña (4+ caracteres)
```

---

| Lista con búsqueda y filtros | Info completa + edición | Preferencias con DataStore |

**Características visuales:**
- LazyColumn con cards
- Búsqueda instantánea
- Diálogos Material 3
- Emojis dinámicos por género
- Animaciones fluidas

---

## 🏗️ Principales decisiones técnicas

### 1. **Room + Flow para reactividad automática**
```kotlin
@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>
}
```
✅ UI se actualiza automáticamente cuando cambian los datos  
✅ Sin observadores manuales  
✅ Compatible con Compose

### 2. **DataStore en lugar de SharedPreferences**
```kotlin
val isLoggedIn: Flow<Boolean> = dataStore.data.map { 
    it[IS_LOGGED_IN] ?: false 
}
```
✅ API asíncrona con Coroutines  
✅ Type-safe con Flow  
✅ Evita bloquear la UI (ANR)

### 3. **StateFlow + combine() en ViewModel**
```kotlin
val uiState = combine(
    movies, searchQuery, showFavorites, sortOrder
) { movies, query, fav, sort ->
    MoviesUiState(filteredMovies = ...)
}.stateIn(viewModelScope, ...)
```
✅ Única fuente de verdad  
✅ Combina múltiples flujos reactivamente  
✅ Estado siempre consistente

### 4. **Repository Pattern con Mappers**
```kotlin
fun MovieEntity.toMovie(): Movie
fun Movie.toEntity(): MovieEntity
```
✅ Desacopla Room del dominio  
✅ Facilita testing  
✅ Permite cambiar implementación sin afectar UI

### 5. **Navegación tipada**
```kotlin
sealed class Routes(val route: String) {
    object Detail : Routes("detail/{movieId}") {
        fun createRoute(id: Int) = "detail/$id"
    }
}
```
✅ Previene errores de typo  
✅ Refactoring seguro  
✅ Argumentos validados en compilación

### 6. **Service Locator simple**
```kotlin
object ServiceLocator {
    fun provideMovieRepository(context: Context): MovieRepository
}
```
✅ Más simple que Hilt para proyecto académico  
✅ Sin generación de código  
✅ Fácil de debuggear

---

## ✅ Cumplimiento de requisitos

| Requisito | Implementación | Archivo |
|-----------|---------------|---------|
| **UI Compose + M3** | LazyColumn, Cards, Dialogs | `ui/home/HomeScreen.kt` |
| **Navegación tipada** | Routes sealed class | `navigation/Routes.kt` |
| **DataStore (2+ claves)** | 5 preferencias con Flow | `data/prefs/UserPrefsRepository.kt` |
| **Room (Flow + CRUD)** | MovieDao con Flow | `data/local/dao/MovieDao.kt` |
| **MVVM + Coroutines** | StateFlow + viewModelScope | `ui/movies/MoviesViewModel.kt` |
| **Repository + Mappers** | Entity ↔ Model | `data/repository/MovieRepository.kt` |

### Extras implementados
- Búsqueda en tiempo real
- Múltiples ordenamientos
- Sistema de favoritos
- Validaciones exhaustivas
- Tema oscuro/claro (no funciona, pero si cambias el del sitema si)

---

## 📊 Estructura de datos

### Room Database
```sql
CREATE TABLE movies (
    id INTEGER PRIMARY KEY,
    title TEXT NOT NULL,
    director TEXT NOT NULL,
    year INTEGER,
    genre TEXT,
    rating REAL,
    isFavorite INTEGER,
    watchedDate INTEGER,
    personalNotes TEXT
)
```

### DataStore Preferences
- `is_logged_in`: Boolean
- `username`: String
- `theme_mode`: "system" | "light" | "dark"
- `sort_order`: "title" | "year" | "rating" | "date"
- `show_favorites_only`: Boolean

---

## 🛠️ Stack tecnológico

- **Kotlin** 1.9.10
- **Jetpack Compose** BOM 2024.01.00
- **Material 3** - Design system
- **Room** 2.6.1 - Base de datos local
- **DataStore** 1.0.0 - Preferencias
- **Navigation Compose** 2.7.5
- **Coroutines + Flow** 1.7.3
- **ViewModel** 2.6.2

---

## 📂 Arquitectura

```
data/
├── local/          # Room (Entity, Dao, Database)
├── mappers/        # Entity ↔ Model
├── model/          # Domain models
├── prefs/          # DataStore
└── repository/     # Abstracción de datos

ui/
├── home/           # Pantalla principal
├── detail/         # Detalle de película
├── settings/       # Ajustes
└── movies/         # ViewModel (StateFlow)

navigation/         # Routes + NavGraph
theme/              # Colors, Typography, Theme
```

**Flujo de datos:**
```
UI (Compose) → ViewModel (StateFlow) → Repository → Room/DataStore
     ↑                                                     ↓
     └─────────────────── Flow ──────────────────────────┘
```

---

## 👨‍💻 Autores

**Jorge Veres y Airam Ceballo**  
Proyecto Final - UT2  
Fecha de entrega: 29/11/2025

---

## 📚 Referencias

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- [Material Design 3](https://m3.material.io/)

---

**Versión:** 1.0.0 | **Fecha de entrega:** 29/11/2025
