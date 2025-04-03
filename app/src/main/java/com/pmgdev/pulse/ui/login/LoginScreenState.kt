package com.pmgdev.pulse.ui.login

data class LoginScreenState(
    val email:String = "",
    val password:String = "",

    val onEmailChange:String = "",
    val onPasswordChange:String = "",

    val success:Boolean = false,
    val emptyFields:Boolean = false,
    val generalError:Boolean = false
)