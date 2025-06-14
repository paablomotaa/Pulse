package com.pmgdev.pulse.repository.model


/**
 *
 * Clase DailyMission
 *
 * Representa cada una de las misiones diarias que se irán añadiendo
 *
 */
data class DailyMission(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val goal: Int = 0,
    val progress: Int = 0,
    val isCompleted: Boolean = false,
    val daily: Boolean = true,
    val active: Boolean = true,
)
