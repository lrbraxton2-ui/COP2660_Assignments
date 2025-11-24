package com.example.flightsearch.data

import android.content.Context
import java.io.IOException
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val USER_PREFERENCES_NAME = "user_preferences"


private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class PreferencesRepository(private val context: Context) {

    companion object {
        private val SEARCH_TEXT_KEY = stringPreferencesKey("search_text")
    }

    val searchTextFlow: Flow<String> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map { preferences ->
                preferences[SEARCH_TEXT_KEY] ?: ""
            }

    suspend fun saveSearchText(text: String) {
        context.dataStore.edit { prefs: MutablePreferences ->
            prefs[SEARCH_TEXT_KEY] = text
        }
    }
}
