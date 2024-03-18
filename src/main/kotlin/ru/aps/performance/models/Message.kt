package ru.aps.performance.models

import java.sql.Timestamp

data class Message(
    val chatRoomId: String,
    val senderId: String,
    val body: String,
    val sendTime: Timestamp
) {
    override fun toString(): String {
        val result = StringBuilder()
        result.append(senderId).append("\t(").append(sendTime).append(")\n").append(body).append("\n")
        return String(result)
    }
}