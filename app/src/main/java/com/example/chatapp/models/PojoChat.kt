package com.example.chatapp.models

data class PojoChat(
    val message: String = "",
    val fromUId: String = "",
    val toUId: String = ""
)