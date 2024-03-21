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
    open var userId: UUID? = null,
    open var chatRoomId: UUID? = null
) {
    class UserXChatRoomBuilder {
        var userXChatRoom = UserXChatRoom()
        fun uid(uid: Long): UserXChatRoomBuilder {
            userXChatRoom.uid = uid
            return this
        }

        fun userId(userId: UUID): UserXChatRoomBuilder {
            userXChatRoom.userId = userId
            return this
        }

        fun chatRoomId(chatRoomId: UUID): UserXChatRoomBuilder {
            userXChatRoom.chatRoomId = chatRoomId
            return this
        }

        fun build(): UserXChatRoom {
            return userXChatRoom
        }
    }
}