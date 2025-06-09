package com.example.chatapp.model

data class Channel(
    var id: String,
    var name: String,
    var createdAt: Long = System.currentTimeMillis()
)