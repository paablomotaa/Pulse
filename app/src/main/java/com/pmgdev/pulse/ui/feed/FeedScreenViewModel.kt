package com.pmgdev.pulse.ui.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 *
 * FeedViewModel
 *
 * Esto no es más que una vista previa muy lejana de lo que será realmente el feed.
 *
 */
@HiltViewModel
class FeedScreenViewModel @Inject constructor() : ViewModel() {
    var state by mutableStateOf<FeedScreenState>(FeedScreenState.Loading)

    init {
        getNotices()
    }

    fun getNotices(){
        viewModelScope.launch {
            delay(2000)
            state = FeedScreenState.Success
        }
    }
}