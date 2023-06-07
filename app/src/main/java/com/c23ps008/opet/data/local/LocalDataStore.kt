package com.c23ps008.opet.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("OPet_DataStore")

class LocalDataStore(private val context: Context) {

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN]
        }
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }
    }

    suspend fun deleteToken() {
        context.dataStore.edit { preferences -> preferences.remove(ACCESS_TOKEN) }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

}