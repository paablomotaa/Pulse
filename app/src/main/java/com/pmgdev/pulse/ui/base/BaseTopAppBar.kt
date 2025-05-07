package com.pmgdev.pulse.ui.base

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.darkgray
import com.pmgdev.pulse.ui.theme.darkgreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(
    title:String,
    actions:List<Action> = emptyList(),
    navIcon: ImageVector,
    navAction:()->Unit,
){
    TopAppBar(
        title = { Text(title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = darkgray,
            titleContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = navAction) {
                Icon(imageVector = navIcon, contentDescription = "", tint = Color.White)
            }
        },
        actions = {
            actions.forEach { action ->
                IconButton(onClick = action.onClick) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = action.contentDescription,
                        tint = Color.White
                    )
                }

            }
        }
    )
}

data class Action(
    val icon: ImageVector,
    val contentDescription: String = "",
    val onClick: () -> Unit
)