package com.pmgdev.pulse.ui.utilities

data class UtilitiesState(
    val altura: String = "",
    val peso: String = "",
    val objetivo: String = "Quiero mejorar mi dieta en general",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val dietAdvice: String? = null
)
