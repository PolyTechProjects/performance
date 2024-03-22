package ru.aps.performance.models

import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Entity
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import java.util.UUID

@Entity
@Table(name = "user_and_chat_room")
open class UserXChatRoom(
    @Id
    @GeneratedValue
    open var uid: Long? = null,
    open var userId: UUID,
    open var chatRoomId: UUID
)