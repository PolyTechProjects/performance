package ru.aps.performance.repos

import ru.aps.performance.models.Message

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
    fun findByChatId(chatId: Long): List<Message>
}
