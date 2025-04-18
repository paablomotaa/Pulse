package com.pmgdev.pulse.repository.model

import android.graphics.Paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class Post(
    val username:String,
    val image: Painter,
    val imagePost: Painter
)