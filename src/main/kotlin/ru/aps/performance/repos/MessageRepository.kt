package ru.aps.performance.repos

import org.springframework.data.repository.CrudRepository
import org.springframework.data.jpa.repository.Query
import ru.aps.performance.models.Message
import java.util.UUID

interface MessageRepository: CrudRepository<Message, Long> {
    @Query(
        value = "SELECT COUNT(m) FROM messages m WHERE m.chat_room_id = :chatRoomId",
        nativeQuery=true
    )
    fun countByChatRoomId(chatRoomId: UUID): Int
    fun findAllByChatRoomId(chatRoomId: UUID): List<Message>
}