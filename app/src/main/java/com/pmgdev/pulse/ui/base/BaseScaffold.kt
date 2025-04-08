package com.pmgdev.pulse.ui.base

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun BaseScaffold(
    title: String,
    navController: NavController,
    showBottomBar: Boolean = true,
    actions: List<Action> = emptyList(),
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { BaseTopAppBar(title = title, actions = actions) },
        bottomBar = {
            if (showBottomBar) {
                BaseNavigationBar(navController)
            }
        },
        content = content
    )
}