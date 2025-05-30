package com.pmgdev.pulse.ui.base.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pmgdev.pulse.ui.theme.mediumgreen

@Composable
fun BaseScaffold(
    title: String,
    navController: NavController,
    showBottomBar: Boolean = true,
    showActionButton:Boolean = false,
    floatingAction: () -> Unit = {},
    actions: List<Action> = emptyList(),
    navIcon:ImageVector? = null,
    navIconAction:()-> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = { BaseTopAppBar(title = title, actions = actions, navIcon = navIcon, navAction = navIconAction) },
        bottomBar = {
            if (showBottomBar) {
                BaseNavigationBar(navController)
            }
        },
        floatingActionButton = {
            if(showActionButton){
                FloatingActionButton(onClick = {
                    floatingAction()
                },
                    modifier = Modifier.offset(y = 30.dp),
                    containerColor = mediumgreen,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = content
    )
}