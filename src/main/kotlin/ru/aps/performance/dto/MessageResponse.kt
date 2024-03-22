package ru.aps.performance.dto

import java.sql.Timestamp

class MessageResponse(
    val chatRoomId: String,
    val senderId: String,
    val body: String,
    val sendTime: Timestamp
)