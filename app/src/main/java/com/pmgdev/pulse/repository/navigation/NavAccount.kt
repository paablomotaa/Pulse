package com.pmgdev.pulse.repository.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pmgdev.pulse.ui.login.LoginScreen
import com.pmgdev.pulse.ui.signup.RegisterScreen

object NavAccount {

    const val ROUTE =  "navaccount"

    fun login() = "$ROUTE/login"
    fun register() = "$ROUTE/register"

    fun NavGraphBuilder.navAccount(
        navController: NavController
    ){
        navigation(startDestination = login(), route = ROUTE){
            loginAccount(navController)
            registerAccount(navController)
        }
    }

    private fun NavGraphBuilder.loginAccount(navController: NavController){
        composable(route = login()){
            /*LoginScreen(
                goToRegister = {navController.navigate(register())}
            )*/
        }
    }
    private fun NavGraphBuilder.registerAccount(navController: NavController){
        composable(route = register()){
            /*RegisterScreen(
                goToLogin = {navController.navigate(login())}
            )*/
        }
    }
}