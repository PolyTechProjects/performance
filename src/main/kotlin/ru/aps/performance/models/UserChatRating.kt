package ru.aps.performance.models

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.Column
import java.util.UUID

@Entity
@Table(name = "user_chat_ratings")
class UserChatRating(
    @Id
    @Column(name = "rating_id")
    var id: UUID = UUID.randomUUID(),

    @Column(name = "chat_room_id")
    var chatRoomId: UUID,

    @Column(name = "user_id")
    var userId: UUID,

    var creativity: Double = 0.0,
    var friendliness: Double = 0.0
)
