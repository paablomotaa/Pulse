package com.pmgdev.pulse.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


/**
 *
 * Clase Session
 *
 * Para gestionar la sesión del usuario y las preferencias.
 *
 */
@Singleton
class Session @Inject constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        const val DATA = "Data"
        private const val IS_LOGIN = "IsLogin"
        private const val EMAIL = "Email"
        private const val PASSWORD = "Password"
        private const val USERNAME = "UserName"
        private const val REALTIME_STEPS_TODAY = "realtime_steps_today"
        private const val LAST_STEP_DATE = "last_step_date"

        val email = stringPreferencesKey(EMAIL)
        val isLogin = booleanPreferencesKey(IS_LOGIN)
        val password = stringPreferencesKey(PASSWORD)
        val userName = stringPreferencesKey(USERNAME)
        val realtimeStepsTodayKey = intPreferencesKey(REALTIME_STEPS_TODAY)
        val lastStepDateKey = longPreferencesKey(LAST_STEP_DATE)
    }

    /**
     *
     * isUserLoggedIn
     *
     * Devuelve un Flow<Boolean> que indica si el usuario ha iniciado sesión o no.
     *
     *
     *
     */

    fun isUserLoggedIn(): Flow<Boolean> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preference ->
            preference[isLogin] ?: false
        }
    }

    /**
     *
     * saveUserSession
     *
     * Guarda la sesión del usuario en las preferencias.
     *
     * @param userEmail
     * @param userPassword
     * @param isUserLoggedIn
     *
     */

    suspend fun saveUserSession(userEmail: String, userPassword: String, isUserLoggedIn: Boolean) {
        dataStore.edit { preference ->
            preference[email] = userEmail
            preference[password] = userPassword
            preference[isLogin] = isUserLoggedIn
        }
    }

    /**
     *
     * clearSession
     *
     * Borra la sesión del usuario.
     *
     */
    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}