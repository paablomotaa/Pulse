package com.pmgdev.pulse.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Session @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        const val DATA = "Data"
        private const val IS_LOGIN = "IsLogin"
        private const val EMAIL = "Email"
        private const val PASSWORD = "Password"
        private const val USERNAME = "UserName"
        val email = stringPreferencesKey(EMAIL)
        val isLogin = booleanPreferencesKey(IS_LOGIN)
        val password = stringPreferencesKey(PASSWORD)
        val userName = stringPreferencesKey(USERNAME)
    }

    fun isUserLoggedIn(): Flow<Boolean> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preference ->
            preference[isLogin] ?: false
        }
    }

    suspend fun saveUserSession(userEmail: String, userPassword: String, isUserLoggedIn: Boolean) {
        dataStore.edit { preference ->
            preference[email] = userEmail
            preference[password] = userPassword
            preference[isLogin] = isUserLoggedIn
        }
    }

    suspend fun setUserLoggedIn(isUserLoggedIn: Boolean) {
        dataStore.edit { preference ->
            preference[isLogin] = isUserLoggedIn
        }
    }

    fun getEmail(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            preferences[email] ?: ""
        }
    }

    suspend fun setEmail(userEmail: String) {
        dataStore.edit { preference ->
            preference[email] = userEmail
        }
    }

    fun getPassword(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { value: Preferences ->
            value[password] ?: ""
        }
    }

    suspend fun setPassword(userPassWord: String) {
        dataStore.edit { preference ->
            preference[password] = userPassWord
        }
    }


    fun getUser(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { value: Preferences ->
            value[userName] ?: ""
        }
    }

    suspend fun setUser(user: String) {
        dataStore.edit { preference ->
            preference[userName] = USERNAME
        }
    }
    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}