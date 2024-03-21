package ru.aps.performance.dto

class ChatRoomRequest(
    val chatRoomId: String? = null,
    val firstUserId: String,
    val secondUserId: String? = null,
    val name: String? = null
)