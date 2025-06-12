package com.pmgdev.pulse.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.Calendar
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
        private const val REALTIME_STEPS_TODAY = "realtime_steps_today"
        private const val LAST_STEP_DATE = "last_step_date"

        val email = stringPreferencesKey(EMAIL)
        val isLogin = booleanPreferencesKey(IS_LOGIN)
        val password = stringPreferencesKey(PASSWORD)
        val userName = stringPreferencesKey(USERNAME)
        val realtimeStepsTodayKey = intPreferencesKey(REALTIME_STEPS_TODAY)
        val lastStepDateKey = longPreferencesKey(LAST_STEP_DATE)
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
    suspend fun saveRealtimeStepsToday(steps: Int) {
        dataStore.edit { preference ->
            preference[realtimeStepsTodayKey] = steps
            preference[lastStepDateKey] = System.currentTimeMillis() // Guarda la hora actual
        }
    }
    fun getRealtimeStepsToday(): Flow<Int> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            val lastSavedDate = preferences[lastStepDateKey] ?: 0L
            val today = System.currentTimeMillis()


            if (!isSameDay(lastSavedDate, today)) {
                0
            } else {
                preferences[realtimeStepsTodayKey] ?: 0
            }
        }
    }
    private fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = timestamp1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = timestamp2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }


}