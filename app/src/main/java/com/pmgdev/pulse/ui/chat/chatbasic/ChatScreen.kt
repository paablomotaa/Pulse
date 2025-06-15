package com.pmgdev.pulse.ui.chat.chatbasic

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pmgdev.pulse.ui.base.composables.Action
import com.pmgdev.pulse.ui.base.composables.BaseMessageBubble
import com.pmgdev.pulse.ui.base.composables.BaseScaffold
import com.pmgdev.pulse.ui.base.baseicons.arrowBack
import com.pmgdev.pulse.ui.theme.clairgreen
import com.pmgdev.pulse.ui.theme.dark



// Poner nombre en el appbar de la persona y el toast cuando se cree una Fine.
/**
 *
 * Interfaz del chat
 *
 * Interfaz del chat básico donde se listarán los mensajes.
 *
 */
@Composable
fun ChatScreen(
    chatId: String,
    viewModel: ChatScreenViewModel,
    navController: NavController,
    goToList: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadMessages(chatId)
        viewModel.observeMessages(chatId)
        viewModel.getUsernameFromOtherParticipant(chatId)
    }
    val messages by viewModel.messages

    BaseScaffold(
        title = viewModel.username,
        navController = navController,
        showBottomBar = false,
        actions = listOf(
            Action(
                icon = Icons.Default.Warning,
                contentDescription = "",
                onClick = {viewModel.createFine(chatId)}
            )
        ),
        navIcon = arrowBack(),
        navIconAction = {goToList()}
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize().background(Brush.verticalGradient(colors = listOf(clairgreen, dark)))) {
            LazyColumn(
                modifier = Modifier.weight(1f).padding(8.dp),
                reverseLayout = true
            ) {
                items(messages.reversed()) { message ->
                    if(message.text.isNullOrBlank()){
                        Log.d("Chat"," no se envía mensaje")
                    }
                    else{
                        BaseMessageBubble(message = message,viewModel.messageIsFrom(message.senderId))
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                }
            }

            Divider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = viewModel.text,
                    onValueChange = { viewModel.onTextChange(it)},
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    placeholder = { Text("Escribe un mensaje...") },
                    shape = RoundedCornerShape(24.dp),
                )

                IconButton(onClick = {
                   viewModel.sendMessage(chatId)
                }){
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }
        }
    }
}