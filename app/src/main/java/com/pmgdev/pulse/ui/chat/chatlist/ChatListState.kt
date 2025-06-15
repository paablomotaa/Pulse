package com.pmgdev.pulse.ui.chat.chatlist


sealed class ChatListState {
    data object NoData : ChatListState()
    data object isLoading : ChatListState()
    data object Success : ChatListState()
}