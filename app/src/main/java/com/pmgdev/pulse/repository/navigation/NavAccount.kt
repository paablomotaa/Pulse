package com.pmgdev.pulse.repository.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pmgdev.pulse.ui.login.LoginScreen
import com.pmgdev.pulse.ui.login.LoginViewModel
import com.pmgdev.pulse.ui.signup.RegisterScreen
import com.pmgdev.pulse.ui.signup.RegisterViewModel

/**
 *
 * Grafo de navegacion del login y el register.
 *
 */

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
            val viewModel:LoginViewModel = hiltViewModel()
            LoginScreen(
                goToRegister = { navController.navigate(register()) },
                viewModel = viewModel,
                goToHome = {navController.navigate(NavHome.feedscreen())}
            )
        }
    }
    private fun NavGraphBuilder.registerAccount(navController: NavController){
        composable(route = register()){
            val viewModel:RegisterViewModel = hiltViewModel()

            RegisterScreen(
                goToLogin = {navController.navigate(login())},
                viewModel = viewModel,
            )
        }
    }
}