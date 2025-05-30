package com.pmgdev.pulse.ui.missions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.pmgdev.pulse.repository.model.DailyMission
import com.pmgdev.pulse.repository.model.MissionType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DailyMissionsViewModel @Inject constructor() : ViewModel() {

    var missions by mutableStateOf<List<DailyMission>>(emptyList())
        private set

    /**
     *
     * loadMissions
     *
     * Carga de misiones por ahora en local.
     *
     */
    fun loadMissions(steps: Int, calories: Float) {
        missions = listOf(
            DailyMission(
                id = "1",
                title = "Camina 5000 pasos",
                description = "Camina al menos 5000 pasos hoy.",
                goal = 5000,
                progress = steps,
                type = MissionType.STEPS,
                isCompleted = steps >= 5000
            ),
            DailyMission(
                id = "2",
                title = "Quema 200 calorías",
                description = "Quema al menos 200 calorías hoy.",
                goal = 200,
                progress = calories.toInt(),
                type = MissionType.CALORIES,
                isCompleted = calories >= 200f
            )
        )
    }
}
