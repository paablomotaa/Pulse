package com.pmgdev.pulse.repository.model

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