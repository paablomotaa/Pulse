package com.pmgdev.pulse.ui.notifications

import com.pmgdev.pulse.repository.model.Notification

sealed class NotificationsScreenState {
    data object isLoading: NotificationsScreenState()
    data class Success(val notifications: List<Notification>): NotificationsScreenState()
    data object NoData: NotificationsScreenState()
}