package com.pmgdev.pulse.ui.base


import android.graphics.drawable.Icon
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pmgdev.pulse.ui.theme.dark

@Composable
fun BaseNavigationBar() {
    NavigationBar(
        modifier = Modifier.height(50.dp).border(1.dp, Color.White, shape = RoundedCornerShape(16.dp)),
        containerColor = dark
    ) {
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = "", tint = Color.White) })
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Notifications, contentDescription = "",tint = Color.White) })
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Person, contentDescription = "",tint = Color.White) })
    }

}