package com.pmgdev.pulse

import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.pmgdev.pulse.repository.navigation.NavAccount
import com.pmgdev.pulse.repository.navigation.NavAccount.navAccount
import com.pmgdev.pulse.repository.navigation.NavHome
import com.pmgdev.pulse.repository.navigation.NavHome.navHome
import com.pmgdev.pulse.ui.base.BaseScaffold
import com.pmgdev.pulse.ui.base.LoadingScreen
import com.pmgdev.pulse.ui.feed.FeedScreen
import com.pmgdev.pulse.ui.login.LoginScreen
import com.pmgdev.pulse.ui.login.LoginViewModel
import com.pmgdev.pulse.ui.notifications.NotificationsScreen
import com.pmgdev.pulse.ui.signup.RegisterScreen
import com.pmgdev.pulse.ui.signup.RegisterViewModel
import com.pmgdev.pulse.ui.theme.PulseTheme
import com.pmgdev.pulse.ui.userprofile.ProfileScreen
import com.pmgdev.pulse.ui.utilities.UtilitiesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PulseTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) {
                    val loginViewModel: LoginViewModel = viewModel()
                    val registerViewModel:RegisterViewModel  = viewModel()
                    val navController = rememberNavController()
                    /*NavHost(navController = navController, startDestination = NavHome.ROUTE){
                        navHome(navController = navController)
                    }*/

                    RegisterScreen(
                        {},
                        registerViewModel
                    )
                }
            }
        }
    }
}