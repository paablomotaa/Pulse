package com.pmgdev.pulse.ui.previewpost

import com.pmgdev.pulse.repository.model.Comment
import com.pmgdev.pulse.repository.model.Post

sealed class PreviewPostState{
    data object NoData: PreviewPostState()
    data object Loading:PreviewPostState()
    data class Success(val post:Post,val comments:List<Comment>):PreviewPostState()
}