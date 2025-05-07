package com.pmgdev.pulse.ui.feed

import com.pmgdev.pulse.repository.model.Post

sealed class FeedScreenState{
    data object NoData: FeedScreenState()
    data object Loading:FeedScreenState()
    data class Success(val post:List<Post>):FeedScreenState()
}