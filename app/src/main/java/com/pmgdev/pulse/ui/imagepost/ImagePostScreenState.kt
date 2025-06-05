package com.pmgdev.pulse.ui.imagepost

data class ImagePostScreenState(
    val image: String = "",
    val description:String = "",

    val isErrorImage: Boolean = false,

    val errorTextImage:String = "",
    val toastMessage:String? = null,

    val globalError:Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorTextGlobal:String = ""
)