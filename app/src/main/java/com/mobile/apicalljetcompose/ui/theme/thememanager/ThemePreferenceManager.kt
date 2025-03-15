package com.mobile.apicalljetcompose.ui.theme.thememanager
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class ThemePreferenceManager(private val context: Context) {
    private val THEME_KEY = booleanPreferencesKey("dark_theme")

    val themeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[THEME_KEY] ?: false }

    suspend fun saveTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }
}
