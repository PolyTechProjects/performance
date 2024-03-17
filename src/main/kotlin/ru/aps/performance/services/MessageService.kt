package ru.aps.performance.services

import ru.aps.performance.models.Message
import ru.aps.performance.repos.MessageRepository

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.time.LocalDateTime

@Service
class MessageService(private val messageRepository: MessageRepository) {

    @Transactional
    fun sendMessage(chatId: Long, senderId: String, content: String): Message {
        return messageRepository.save(Message(
            chatId = chatId,
            senderId = senderId,
            content = content,
            timestamp = LocalDateTime.now()
        ))
    }

    fun getMessagesForChat(chatId: Long): List<Message> {
        return messageRepository.findByChatId(chatId)
    }
}
