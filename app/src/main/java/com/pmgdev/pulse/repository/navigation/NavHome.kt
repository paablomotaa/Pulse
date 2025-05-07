package com.pmgdev.pulse.repository.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pmgdev.pulse.ui.feed.FeedScreen
import com.pmgdev.pulse.ui.feed.FeedScreenViewModel
import com.pmgdev.pulse.ui.imagepost.ImagePostScreen
import com.pmgdev.pulse.ui.imagepost.ImagePostViewModel
import com.pmgdev.pulse.ui.notifications.NotificationsScreen
import com.pmgdev.pulse.ui.previewpost.PostDetailScreen
import com.pmgdev.pulse.ui.previewpost.PreviewPostViewModel
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
    fun imagepostscreen() = "$ROUTE/imagepost"
    fun postPreview(postId: String = "{postId}") = "$ROUTE/postpreview/$postId"

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
            imagepost(navController)
            postPreview(navController)
        }
    }

    private fun NavGraphBuilder.home(navController: NavController){
        composable(route = feedscreen()){
            val viewModel:FeedScreenViewModel = hiltViewModel()

            FeedScreen(
                navController,
                viewModel = viewModel,
                goToImagePost = {navController.navigate(imagepostscreen())},
                goToPostPreview = { postId ->
                    navController.navigate(NavHome.postPreview(postId))
                }
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
    private fun NavGraphBuilder.imagepost(navController: NavController){
        composable(route = imagepostscreen()){
            val viewModel: ImagePostViewModel = hiltViewModel()
            ImagePostScreen(navController,viewModel,{navController.popBackStack()})
        }
    }
    private fun NavGraphBuilder.postPreview(navController: NavController) {
        composable(
            route = "$ROUTE/postpreview/{postId}"
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            val viewModel: PreviewPostViewModel = hiltViewModel()
            PostDetailScreen(
                post = postId, viewModel = viewModel,
                onBack = { navController.popBackStack()},
                navController = navController,
            )
        }
    }
}