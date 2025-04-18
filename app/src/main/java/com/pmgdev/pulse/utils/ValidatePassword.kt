package com.pmgdev.pulse.utils

//Funcion para que el formato de la contraseña sea correcto
fun ValidatePassword(password:String): Boolean{
    val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")
    return regex.matches(password)
}