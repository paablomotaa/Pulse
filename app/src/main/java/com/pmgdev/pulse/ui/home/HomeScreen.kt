package com.pmgdev.pulse.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.pmgdev.pulse.repository.navigation.NavAccount
import com.pmgdev.pulse.repository.navigation.NavAccount.navAccount
import com.pmgdev.pulse.repository.navigation.NavHome
import com.pmgdev.pulse.repository.navigation.NavHome.navHome

@Composable
fun HomeScreen(viewModel: SessionViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    var sessionChecked by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.isLoggedIn) {
        sessionChecked = true
    }

    if(sessionChecked){
        NavHost(navController = navController,
            startDestination = if(isLoggedIn) NavHome.ROUTE else NavAccount.ROUTE
        ) {
            navAccount(navController)
            navHome(navController)
        }
    }
}
