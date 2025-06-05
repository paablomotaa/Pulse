package com.pmgdev.pulse

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.pmgdev.pulse.repository.navigation.NavAccount
import com.pmgdev.pulse.repository.navigation.NavAccount.navAccount
import com.pmgdev.pulse.repository.navigation.NavHome.navHome
import com.pmgdev.pulse.ui.theme.PulseTheme
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize
import com.google.firebase.storage.FirebaseStorage
import com.pmgdev.pulse.ui.fitness.FitnessScreenViewModel
import com.pmgdev.pulse.ui.home.HomeScreen
import com.pmgdev.pulse.ui.settings.SettingsViewModel
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
        )
        setContent {
            PulseTheme {
                    HomeScreen()
            }
        }
    }
    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FitnessScreenViewModel.GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
            val context = this
            val viewModel: FitnessScreenViewModel by viewModels()
                if (viewModel.checkGoogleFitPermissions(context)) {
                    viewModel.requestSteps(context)
                    viewModel.requestCalories(context)
                }
        }
    }*/
}