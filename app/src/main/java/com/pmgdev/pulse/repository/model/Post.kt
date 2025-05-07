package com.pmgdev.pulse.repository.model

import androidx.compose.ui.graphics.painter.Painter
import java.util.Date

data class Post(
    val uid:String, //Esto se actualiza con el id del documento que genera firestore
    val uiduser:String, //UIDD de auth para la info
    val username:String,
    val imagePost: String,
    val description:String,
    val date:Date = Date(),
    val likes:Int = 0,
    val comments:Int = 0,
){
    constructor() : this("","","","","")
}