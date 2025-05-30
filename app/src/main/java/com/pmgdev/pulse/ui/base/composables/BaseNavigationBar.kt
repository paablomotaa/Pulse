package com.pmgdev.pulse.ui.base.composables


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pmgdev.pulse.repository.navigation.NavHome
import com.pmgdev.pulse.ui.base.baseicons.fitnessIcon
import com.pmgdev.pulse.ui.base.baseicons.leafIcon
import com.pmgdev.pulse.ui.theme.dark
import com.pmgdev.pulse.ui.theme.mediumgreen

@Composable
fun BaseNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Inicio", Icons.Default.Home, NavHome.feedscreen()),
        BottomNavItem("Dieta", leafIcon(), NavHome.utilitiesscreen()),
        BottomNavItem("Fitness", fitnessIcon(),NavHome.fitnessscreen()),
        BottomNavItem("Avisos", Icons.Default.Notifications, NavHome.notificationsscreen()),
        BottomNavItem("Perfil", Icons.Default.Person, NavHome.profilescreen())
    )
    NavigationBar(
        contentColor = Color.White,
        containerColor = dark
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title, tint = Color.White) },
                label = { Text(item.title, color = Color.White) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(NavHome.ROUTE) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White,
                    indicatorColor = mediumgreen
                )
            )
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)