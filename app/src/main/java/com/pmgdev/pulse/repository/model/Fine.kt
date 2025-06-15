package com.pmgdev.pulse.repository.model

data class Fine (
    val userId:String,
    val reportedId:String,
    val TypeFine : TypeFine,
)

enum class TypeFine{
    POST,
    CHAT,
}