package com.pmgdev.pulse.ui.signup

data class RegisterScreenState (
    val name:String = "",
    val date:String = "",
    val email:String = "",
    val password:String = "",

    val isNameError:Boolean = false,
    val isDateError:Boolean = false,
    val isEmailError:Boolean = false,
    val isPasswordError:Boolean = false,

    val nameErrorText:String = "",
    val dateErrorText:String = "",
    val emailErrorText:String = "",
    val passwordErrorText:String = "",

    val success:Boolean = false,
    val isGlobalError:Boolean = false
)