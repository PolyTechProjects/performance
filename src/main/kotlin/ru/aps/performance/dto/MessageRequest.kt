package ru.aps.performance.dto

import java.sql.Timestamp

class MessageRequest(
    val chatRoomId: String,
    val senderId: String,
    val body: String,
    val sendTime: Timestamp
)