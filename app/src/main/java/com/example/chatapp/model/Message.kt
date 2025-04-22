package com.example.chatapp.model

data class Message(
    val id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val createdAt: String = System.currentTimeMillis().toString(),
    val senderName: String = "",
    val senderImage: String? = null,
    val imageUrl: String? = null
)
