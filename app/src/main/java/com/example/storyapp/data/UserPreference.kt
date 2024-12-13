package com.example.storyapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")


class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveToken(userModel: UserModel) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = userModel.token
            preferences[NAME_KEY] = userModel.name
            preferences[PASSWORD_KEY] = userModel.password
            preferences[EMAIL_KEY] = userModel.email
            preferences[USER_ID_KEY] = userModel.userId

        }
    }

     fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                userId = preferences[USER_ID_KEY] ?: "",
                name = preferences[NAME_KEY] ?: "",
                token = preferences[TOKEN_KEY] ?: "",
                email = preferences[EMAIL_KEY] ?: "",
                password = preferences[PASSWORD_KEY] ?: ""
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private  val TOKEN_KEY = stringPreferencesKey("token")
        private  val NAME_KEY = stringPreferencesKey("name")
        private  val PASSWORD_KEY = stringPreferencesKey("password")
        private  val USER_ID_KEY = stringPreferencesKey("userId")
        private  val EMAIL_KEY = stringPreferencesKey("email")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}