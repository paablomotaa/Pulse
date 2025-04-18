package com.pmgdev.pulse.ui.userprofile

import com.pmgdev.pulse.repository.model.User

sealed class ProfileScreenState {
    data object NoData: ProfileScreenState()
    data object Loading:ProfileScreenState()
    data class Success(var user: User?):ProfileScreenState()
}