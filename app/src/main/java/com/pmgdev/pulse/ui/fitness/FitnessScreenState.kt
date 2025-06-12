package com.pmgdev.pulse.ui.fitness

data class FitnessScreenState(
    val steps: Int = 0,
    val stepsYesterday: Int = 0,
    val calories: Float = 0f,
    val success: Boolean = false,
    val isError: Boolean = false,
    val error: String? = null,
    val isNotRegister: Boolean = false,
)