package com.pmgdev.pulse.ui.chat.chatlist

import com.pmgdev.pulse.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pmgdev.pulse.repository.model.ChatPreview
import com.pmgdev.pulse.ui.base.baseicons.arrowBack
import com.pmgdev.pulse.ui.base.components.LoadingScreen
import com.pmgdev.pulse.ui.base.components.NoDataScreen
import com.pmgdev.pulse.ui.base.composables.BaseScaffold
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun ChatListScreen(
    viewModel: ChatListScreenViewModel,
    navController: NavController,
    goToChat: (String) -> Unit,
    goToFeed: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadChats()
    }
    val chats = viewModel.chatPreviews.value
    BaseScaffold(
        title = "Chats",
        navController = navController,
        showBottomBar = false,
        navIcon = arrowBack(),
        navIconAction = {goToFeed()},
    ) { paddingValues ->

        when(viewModel.state.value){
            ChatListState.NoData -> {
                NoDataScreen(paddingValues)
            }
            ChatListState.isLoading -> {
                LoadingScreen(paddingValues)
            }
            ChatListState.Success -> {
                ChatScreenContent(paddingValues, chats, goToChat)
            }
        }
    }
}
@Composable
fun ChatScreenContent(paddingValues: PaddingValues,chats: List<ChatPreview>,goToChat: (String) -> Unit){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues).background(Brush.verticalGradient(listOf(clairgreen,dark)))
    ) {
        items(chats) { chat ->
            ChatPreviewItem(chat = chat, onClick = { chatId ->  goToChat(chatId) })
            Divider()
        }
    }
}

@Composable
fun ChatPreviewItem(chat: ChatPreview, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable { onClick(chat.chatId) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = dark),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val imageModel = if(chat.otherUserImageUrl.isNullOrBlank()){
                R.drawable.logo_pulse_transparent
            }
            else{
                chat.otherUserImageUrl
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageModel)
                    .crossfade(true)
                    .build(),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(clairgreen, CircleShape),
            )


            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chat.otherUserName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }

            Text(
                text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(chat.timestamp.toDate()),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }
    }
}






