package com.pmgdev.pulse.repository.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.pmgdev.pulse.ui.chat.chatbasic.ChatScreen
import com.pmgdev.pulse.ui.chat.chatbasic.ChatScreenViewModel
import com.pmgdev.pulse.ui.chat.chatlist.ChatListScreen
import com.pmgdev.pulse.ui.chat.chatlist.ChatListScreenViewModel
import com.pmgdev.pulse.ui.editprofile.EditProfileScreen
import com.pmgdev.pulse.ui.editprofile.EditProfileViewModel
import com.pmgdev.pulse.ui.feed.FeedScreen
import com.pmgdev.pulse.ui.feed.FeedScreenViewModel
import com.pmgdev.pulse.ui.fitness.FitnessScreen
import com.pmgdev.pulse.ui.fitness.FitnessScreenViewModel
import com.pmgdev.pulse.ui.imagepost.ImagePostScreen
import com.pmgdev.pulse.ui.imagepost.ImagePostViewModel
import com.pmgdev.pulse.ui.missions.DailyMissionsScreen
import com.pmgdev.pulse.ui.missions.DailyMissionsViewModel
import com.pmgdev.pulse.ui.notifications.NotificationsScreen
import com.pmgdev.pulse.ui.previewpost.PostDetailScreen
import com.pmgdev.pulse.ui.previewpost.PreviewPostViewModel
import com.pmgdev.pulse.ui.settings.SettingsScreen
import com.pmgdev.pulse.ui.settings.SettingsViewModel
import com.pmgdev.pulse.ui.userprofile.ProfileScreen
import com.pmgdev.pulse.ui.userprofile.ProfileScreenViewModel
import com.pmgdev.pulse.ui.utilities.UtilitiesScreen
import com.pmgdev.pulse.ui.utilities.UtilitiesViewModel

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
    fun profilescreen(userId: String = "{userId}") = "$ROUTE/profile/$userId"
    fun imagepostscreen() = "$ROUTE/imagepost"
    fun postPreview(postId: String = "{postId}") = "$ROUTE/postpreview/$postId"
    fun editProfile() = "$ROUTE/editprofile"
    fun chatBasic(chatId:String) = "$ROUTE/chat/$chatId"
    fun listChats() = "$ROUTE/listchat"
    fun fitnessscreen() = "$ROUTE/fitness"
    fun missionsscreen(steps: Int, kcal: Float) = "$ROUTE/missions_screen/$steps/$kcal"
    fun settingsscreen() = "$ROUTE/settings"


    fun NavGraphBuilder.navHome(
        navController: NavController,
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
            editProfile(navController)
            chatBasic(navController)
            listChats(navController)
            fitnessScreen(navController)
            missionsScreen(navController)
            settingScreen(navController)
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
                    navController.navigate(postPreview(postId))
                },
                goToListChats = {navController.navigate(listChats())},
                goToProfile = { useruid ->
                    navController.navigate(profilescreen(useruid))
                }
            )
        }
    }
    private fun NavGraphBuilder.utilities(navController: NavController){
        composable(route = utilitiesscreen()){
            val viewModel: UtilitiesViewModel = hiltViewModel()
            UtilitiesScreen(viewModel,navController)
        }
    }
    private fun NavGraphBuilder.notifications(navController: NavController){
        composable(route = notificationsscreen()){
            NotificationsScreen(
                navController,
            )
        }
    }
    private fun NavGraphBuilder.profile(navController: NavController) {
        composable(route = "$ROUTE/profile/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val viewModel: ProfileScreenViewModel = hiltViewModel()

            ProfileScreen(
                navController = navController,
                viewModel = viewModel,
                uidUser = userId,
                goToEditProfile = {navController.navigate(editProfile())},
                goToChat = { chatId ->
                    navController.navigate(chatBasic(chatId))
                },
                goToPostPreview = { postId ->
                    navController.navigate(postPreview(postId))
                },
                goToSettings = {
                    navController.navigate(settingsscreen())
                }
            )
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
                goToProfile = { userId ->
                    navController.navigate(profilescreen(userId))
                }
            )
        }
    }
    private fun NavGraphBuilder.editProfile(navController: NavController){
        composable(route = editProfile()){
            val viewModel: EditProfileViewModel = hiltViewModel()
            EditProfileScreen(
                navController,
                viewModel,
                goBack = {navController.popBackStack()},
            )
        }
    }
    private fun NavGraphBuilder.chatBasic(navController: NavController){
        composable(route = "$ROUTE/chat/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            val viewModel: ChatScreenViewModel = hiltViewModel()
            ChatScreen(
                chatId,
                viewModel,
                navController,
                goToList = {navController.navigate(listChats())}
            )
        }
    }
    private fun NavGraphBuilder.listChats(navController: NavController){
        composable(route = listChats()){
            val viewModel: ChatListScreenViewModel = hiltViewModel()
            ChatListScreen(
                viewModel = viewModel,
                navController = navController,
                goToChat = { chatId -> navController.navigate(chatBasic(chatId))},
                goToFeed = { navController.navigate(feedscreen()) }
            )
        }
    }
    private fun NavGraphBuilder.fitnessScreen(navController: NavController){
        composable(route = fitnessscreen()) {
            val viewModel: FitnessScreenViewModel = hiltViewModel()
            FitnessScreen(
                viewModel,
                navController,
                {navController.navigate(missionsscreen(viewModel.uiState.steps,viewModel.uiState.calories))}
            )
        }
    }
    private fun NavGraphBuilder.missionsScreen(navController: NavController) {
        composable(
            route = "$ROUTE/missions_screen/{steps}/{kcal}",
            arguments = listOf(
                navArgument("steps") { type = NavType.IntType },
                navArgument("kcal") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val viewModel: DailyMissionsViewModel = hiltViewModel()
            val steps = backStackEntry.arguments?.getInt("steps") ?: 0
            val kcal = backStackEntry.arguments?.getFloat("kcal") ?: 0f

            DailyMissionsScreen(
                viewModel,
                navController,
                steps = steps,
                kcal = kcal
            )
        }
    }
    private fun NavGraphBuilder.settingScreen(navController: NavController){
        composable(route = settingsscreen()){

            val viewModel:SettingsViewModel = hiltViewModel()

            SettingsScreen(
                navController,
                viewModel,
                goToLogin = {navController.navigate(NavAccount.login()){
                    popUpTo(0)
                }}
            )
        }
    }

}