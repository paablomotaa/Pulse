package com.pmgdev.pulse.ui.settings

data class SettingsScreenState(
    val isDarkThemeEnabled: Boolean = false,
    val toastMessage: String? = null,
    val password: String = "",
    val email: String = "",
    val showEmailDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
)