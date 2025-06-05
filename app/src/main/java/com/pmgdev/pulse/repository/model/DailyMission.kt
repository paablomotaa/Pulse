package com.pmgdev.pulse.repository.model


/**
 *
 * Clase DailyMission
 *
 * Representa cada una de las misiones diarias que se irán añadiendo
 *
 */
data class DailyMission(
    val id: String,
    val title: String,
    val description: String,
    val goal: Int,
    val progress: Int,
    val type: MissionType,
    val isCompleted: Boolean
)

enum class MissionType {
    STEPS,
    CALORIES
}