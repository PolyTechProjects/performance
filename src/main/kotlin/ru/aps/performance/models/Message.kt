package ru.aps.performance.models

import java.sql.Timestamp
import java.util.UUID
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import ru.aps.performance.dto.MessageResponse
import ru.aps.performance.dto.MessageRequest

@Entity
@Table(name="messages")
open class Message(
    @Id
    @GeneratedValue
    open var id: Long? = null,
    open var chatRoomId: UUID,
    open var senderId: UUID,
    open var body: String,
    open var sendTime: Timestamp
) {
    override fun toString(): String {
        val result = StringBuilder()
        result.append(senderId).append("\t(").append(sendTime).append(")\n").append(body).append("\n")
        return String(result)
    }

    companion object {
        fun fromRequest(request: MessageRequest): Message {
            val chatRoomId = UUID.fromString(request.chatRoomId)
            val senderId = UUID.fromString(request.senderId)
            val message = Message(chatRoomId=chatRoomId, senderId=senderId, body=request.body, sendTime=request.sendTime)
            return message
        }
    }


    fun toResponse(): MessageResponse {
        return MessageResponse(chatRoomId=chatRoomId.toString(), senderId=senderId.toString(), body=body, sendTime=sendTime)
    }
}