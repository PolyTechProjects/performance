package ru.aps.performance.models

import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import java.util.UUID

@Entity
@Table(name = "chat_rooms")
open class ChatRoom(
    @Id
    open var uid: UUID,

    open var firstUserId: UUID? = null,
    open var secondUserId: UUID? = null,
    open var name: String? = null
)