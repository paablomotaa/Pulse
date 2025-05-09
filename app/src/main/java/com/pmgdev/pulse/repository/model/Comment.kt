package com.pmgdev.pulse.repository.model

import java.util.Date

/**
 *
 * Clase comment
 *
 * Representa los comentarios de las distintas publicaciones.
 *
 * Subcoleccion de post.
 *
 */

data class Comment (
    val uidUser: String,
    val username: String,
    val content: String,
    val date:Date = Date()
){
    constructor() : this("","","")
}