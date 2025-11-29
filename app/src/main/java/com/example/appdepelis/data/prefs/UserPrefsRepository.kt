package com.example.appdepelis.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPrefsRepository(private val context: Context) {

    private object PreferencesKeys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USERNAME = stringPreferencesKey("username")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val SHOW_FAVORITES_ONLY = booleanPreferencesKey("show_favorites_only")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.IS_LOGGED_IN] ?: false
    }

    val username: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.USERNAME] ?: ""
    }

    val themeMode: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.THEME_MODE] ?: "system"
    }

    val sortOrder: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.SORT_ORDER] ?: "title"
    }

    val showFavoritesOnly: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.SHOW_FAVORITES_ONLY] ?: false
    }

    suspend fun setLoginStatus(isLoggedIn: Boolean, username: String = "") {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.IS_LOGGED_IN] = isLoggedIn
            prefs[PreferencesKeys.USERNAME] = username
        }
    }

    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.THEME_MODE] = mode
        }
    }

    suspend fun setSortOrder(order: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.SORT_ORDER] = order
        }
    }

    suspend fun setShowFavoritesOnly(show: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.SHOW_FAVORITES_ONLY] = show
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}