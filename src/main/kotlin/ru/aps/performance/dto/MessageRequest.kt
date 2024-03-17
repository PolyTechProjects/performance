package ru.aps.performance.dto

class MessageRequest(
    val chatRoomId: String,
    val senderId: String,
    val body: String
)