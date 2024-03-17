package ru.aps.performance.models

import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey

@Entity
@Table(name = "user_and_chat_room")
data class UserXChatRoom(
    @Id
    val uid: String,
    val userId: String,
    val chatRoomId: String
)