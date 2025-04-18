package com.pmgdev.pulse.ui.feed

sealed class FeedScreenState{
    data object NoData: FeedScreenState()
    data object Loading:FeedScreenState()
    data object Success:FeedScreenState()
}