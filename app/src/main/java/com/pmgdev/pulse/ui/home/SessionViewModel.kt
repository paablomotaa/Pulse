package com.pmgdev.pulse.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmgdev.pulse.network.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    session: Session
) : ViewModel() {

    val isLoggedIn = session.isUserLoggedIn()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
}