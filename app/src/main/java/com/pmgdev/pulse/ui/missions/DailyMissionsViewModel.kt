package com.pmgdev.pulse.ui.missions

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log // Importar Log para depuración
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.pmgdev.pulse.repository.firebaserepository.UserRepository
import com.pmgdev.pulse.repository.model.AppConstants
import com.pmgdev.pulse.repository.model.DailyMission
import com.pmgdev.pulse.repository.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DailyMissionsViewModel @Inject constructor(
    application: Application,
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    var missions by mutableStateOf<List<DailyMission>>(emptyList())
        private set

    var userCurrentTitle by mutableStateOf("Sin Título")
        private set

    private val DAILY_STEP_MISSION_ID = AppConstants.DAILY_STEP_MISSION_ID
    private val DAILY_STEP_MISSION_GOAL = AppConstants.DAILY_STEP_MISSION_GOAL
    private val DAILY_STEP_MISSION_TITLE = AppConstants.DAILY_STEP_MISSION_TITLE
    private val DAILY_STEP_MISSION_DESCRIPTION = AppConstants.DAILY_STEP_MISSION_DESCRIPTION
    private val PROGRESSIVE_TITLES = AppConstants.PROGRESSIVE_TITLES

    private val prefs: SharedPreferences =
        application.getSharedPreferences("pulse_app_prefs", Context.MODE_PRIVATE)

    private val KEY_LAST_MISSION_COMPLETION_DATE = "last_mission_completion_date"
    private val KEY_MISSIONS_COMPLETED_COUNT = "missions_completed_count"
    private val KEY_CURRENT_ASSIGNED_TITLE = "current_assigned_title"

    private var isMissionClaimedToday by mutableStateOf(false)


    init {
        loadCurrentState()
    }

    /**
     * loadCurrentState
     * Carga el estado guardado de la aplicación, incluyendo si la misión ha sido reclamada hoy
     * y el título actual del usuario.
     */
    private fun loadCurrentState() {
        val lastDateString = prefs.getString(KEY_LAST_MISSION_COMPLETION_DATE, null)
        val today = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

        if (lastDateString != null) {
            val lastCompletionDate = LocalDate.parse(lastDateString, dateFormatter)
            isMissionClaimedToday = lastCompletionDate == today

        } else {
            isMissionClaimedToday = false
        }

        userCurrentTitle = prefs.getString(KEY_CURRENT_ASSIGNED_TITLE, PROGRESSIVE_TITLES.first())
            ?: PROGRESSIVE_TITLES.first()
    }


    /**
     * loadMissions
     * Carga el estado de la misión diaria basándose solo en los pasos actuales.
     * La propiedad 'isCompleted' de DailyMission ahora indica si la misión ya fue reclamada hoy.
     *
     * @param currentSteps
     */
    fun loadMissions(currentSteps: Int) {
        val currentMissionProgress = currentSteps
        val isCurrentlyAtGoal = currentMissionProgress >= DAILY_STEP_MISSION_GOAL

        missions = listOf(
            DailyMission(
                id = DAILY_STEP_MISSION_ID,
                title = DAILY_STEP_MISSION_TITLE,
                description = DAILY_STEP_MISSION_DESCRIPTION,
                goal = DAILY_STEP_MISSION_GOAL,
                progress = currentMissionProgress,
                isCompleted = isMissionClaimedToday
            )
        )

    }

    /**
     * claimMissionReward
     * Permite al usuario reclamar la recompensa de la misión diaria (el título progresivo).
     * Guarda el estado en SharedPreferences.
     */
    fun claimMissionReward() {
        if (isMissionClaimedToday) {

            return
        }

        val currentMission = missions.firstOrNull()
        if (currentMission == null || currentMission.progress < currentMission.goal) {
            return
        }

        var missionsCompletedCount = prefs.getInt(KEY_MISSIONS_COMPLETED_COUNT, 0)
        missionsCompletedCount++

        val titleIndex = missionsCompletedCount.coerceIn(0, PROGRESSIVE_TITLES.size - 1)
        val newTitle = PROGRESSIVE_TITLES[titleIndex]

        val today = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

        prefs.edit().apply {
            putString(KEY_LAST_MISSION_COMPLETION_DATE, today.format(dateFormatter))
            putInt(KEY_MISSIONS_COMPLETED_COUNT, missionsCompletedCount)
            putString(KEY_CURRENT_ASSIGNED_TITLE, newTitle)
            apply()
        }


        isMissionClaimedToday = true
        userCurrentTitle = newTitle


        //Actualizar el título del usuario
        val uid = auth.currentUser?.uid
        if(uid != null){
            viewModelScope.launch {
                val user = userRepository.getUser(uid)
                if(user != null)

                userRepository.editUser(uid,User(
                    user.uid,
                    user.profileImage,
                    user.username,
                    user.email,
                    user.fullname,
                    user.bio,
                    user.created_at,
                    user.followers,
                    user.following,
                    newTitle
                ))
            }
        }

        missions = missions.map { it.copy(isCompleted = true) }

    }
}