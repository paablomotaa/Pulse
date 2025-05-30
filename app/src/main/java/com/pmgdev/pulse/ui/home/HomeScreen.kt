package com.pmgdev.pulse.ui.home

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.pmgdev.pulse.repository.navigation.NavAccount
import com.pmgdev.pulse.repository.navigation.NavAccount.navAccount
import com.pmgdev.pulse.repository.navigation.NavHome.navHome

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
            NavHost(navController = navController, startDestination = NavAccount.ROUTE) {
                navAccount(navController)
                navHome(navController)
            }
    }
