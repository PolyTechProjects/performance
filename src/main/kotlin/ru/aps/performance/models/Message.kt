package ru.aps.performance.models

import java.sql.Timestamp
import java.util.UUID
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id

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
}