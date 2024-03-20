package ru.aps.performance.services

import org.apache.logging.log4j.LogManager
import org.springframework.amqp.AmqpException
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import java.lang.StringBuilder

import ru.aps.performance.models.Message
import ru.aps.performance.repos.MessageHistoryRepository
import ru.aps.performance.repos.ChatRoomRepository
import ru.aps.performance.exceptions.NoSuchChatRoomException

@Service
class MessageService(
    private val messageHistoryRepository: MessageHistoryRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val rabbitTemplate: RabbitTemplate,
    private val exchange: TopicExchange
) {
    fun sendMessage(message: Message) {
        try {
            rabbitTemplate.convertAndSend("${exchange.name}", message.chatRoomId, message.body)
            if (logger.isTraceEnabled()) {
                logger.trace("INFO: message ${message.body} sent to /queue/${message.chatRoomId}")
            }
        } catch(e: AmqpException) {
            logger.error("ERROR: " + e.message)
        }
        saveMessage(message)
    }

    private fun saveMessage(message: Message) {
        val messageHistory = messageHistoryRepository.findByChatRoomId(message.chatRoomId)
        if (messageHistory == null) {
            throw NoSuchChatRoomException("No chat room with id ${message.chatRoomId} in messagehistory")
        }
        val history = messageHistory.history
        val newHistory = StringBuilder(history).append(message.toString()).toString()
        messageHistoryRepository.updateHistoryById(messageHistory.uid, newHistory)
    }

    companion object {
        val logger = LogManager.getLogger()
    }
}