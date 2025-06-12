package com.pmgdev.pulse.ui.login

data class LoginScreenState(
    val email: String = "",
    val password: String = "",

    val onEmailChange: String = "",
    val onPasswordChange: String = "",

    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,

    val emailErrorText: String = "",
    val passwordErrorText: String = "",
    val toastMessage: String? = null,

    val success: Boolean = false,
    val emptyFields: Boolean = false,
    val generalError: Boolean = false,
    val showEmailDialog: Boolean = false,
)