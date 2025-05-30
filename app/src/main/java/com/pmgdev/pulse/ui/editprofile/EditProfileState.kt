package com.pmgdev.pulse.ui.editprofile

data class EditProfileState(
    val username:String = "",
    val image:String = "",
    val fullname:String = "",
    val bio:String = "",
    val peso:Int = 0,
    val altura:Int = 0,

    val errorUsername: Boolean = false,
    val imageError:Boolean = false,
    val fullnameError: Boolean = false,
    val bioError:Boolean = false,

    val errorUsernameText:String = "",
    val errorImageText:String = "",
    val errorBioText:String = "",
    val errorFullnameText:String = ""
)
