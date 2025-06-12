package com.pmgdev.pulse.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pmgdev.pulse.repository.model.Notification
import com.pmgdev.pulse.ui.base.components.LoadingScreen
import com.pmgdev.pulse.ui.base.components.NoDataScreen
import com.pmgdev.pulse.ui.base.composables.BaseScaffold
import com.pmgdev.pulse.ui.feed.FeedScreenState
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark

@Composable
fun NotificationsScreen(navController: NavController,viewModel: NotificationsScreenViewModel){

    LaunchedEffect(Unit) {
        viewModel.getNotifications()
    }

    BaseScaffold(
        title = "Notificaciones",
        navController = navController,
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues).background(
            Brush.verticalGradient(colors = listOf(clairgreen, dark))),
            contentAlignment = Alignment.TopStart
        ) {

            when(viewModel.state){
                NotificationsScreenState.NoData -> NoDataScreen(paddingValues)
                is NotificationsScreenState.Success -> NotificationScreenContent((viewModel.state as NotificationsScreenState.Success).notifications)
                NotificationsScreenState.isLoading -> LoadingScreen(paddingValues)
            }


        }
    }
}

@Composable
fun NotificationScreenContent(notifications:List<Notification>){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(notifications) { item ->
            NotificationItem(item)
        }
    }
}
@Composable
fun NotificationItem(notification:Notification){
    Card(
        modifier = Modifier.padding(5.dp).width(400.dp).height(70.dp),
        colors = CardColors(containerColor = dark, contentColor = Color.White, disabledContentColor = Color.White, disabledContainerColor = dark),

    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(Icons.Default.Notifications, contentDescription = "", tint = Color.White)
                Text("$notification")
            }

        }
    }
}