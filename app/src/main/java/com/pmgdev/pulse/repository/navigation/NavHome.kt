package com.pmgdev.pulse.repository.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pmgdev.pulse.repository.navigation.NavAccount.login
import com.pmgdev.pulse.repository.navigation.NavAccount.register
import com.pmgdev.pulse.ui.feed.FeedScreen
import com.pmgdev.pulse.ui.feed.FeedScreenViewModel
import com.pmgdev.pulse.ui.notifications.NotificationsScreen
import com.pmgdev.pulse.ui.signup.RegisterScreen
import com.pmgdev.pulse.ui.userprofile.ProfileScreen
import com.pmgdev.pulse.ui.userprofile.ProfileScreenViewModel
import com.pmgdev.pulse.ui.utilities.UtilitiesScreen

/**
 *
 * Grafo de navegación del home una vez inicias sesión.
 *
 */
object NavHome {
    const val ROUTE = "navhome"

    fun feedscreen() = "$ROUTE/home"
    fun utilitiesscreen() = "$ROUTE/utilities"
    fun notificationsscreen() = "$ROUTE/notifications"
    fun profilescreen() = "$ROUTE/profile"

    fun NavGraphBuilder.navHome(
        navController: NavController
    ){
        navigation(
            startDestination = feedscreen(),route = ROUTE
        ){
            home(navController)
            utilities(navController)
            notifications(navController)
            profile(navController)
        }
    }

    private fun NavGraphBuilder.home(navController: NavController){
        composable(route = feedscreen()){
            val viewModel:FeedScreenViewModel = hiltViewModel()

            FeedScreen(
                navController,
                viewModel = viewModel
            )
        }
    }
    private fun NavGraphBuilder.utilities(navController: NavController){
        composable(route = utilitiesscreen()){
            UtilitiesScreen(navController)
        }
    }
    private fun NavGraphBuilder.notifications(navController: NavController){
        composable(route = notificationsscreen()){
            NotificationsScreen(navController)
        }
    }
    private fun NavGraphBuilder.profile(navController: NavController){
        composable(route = profilescreen()){
            val viewModel: ProfileScreenViewModel = hiltViewModel()
            ProfileScreen(navController,viewModel)
        }
    }
}