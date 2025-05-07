package com.pmgdev.pulse.ui.previewpost

data class PreviewPostCommentState(
    val comment:String = "",
    val sended: Boolean = false,
    val error:Boolean = false
)