package com.pmgdev.pulse.repository.model

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class User(
    val username:String,
    val image: Painter,
    val imagePost:Painter
)