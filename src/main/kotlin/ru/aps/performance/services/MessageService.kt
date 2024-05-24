package ru.aps.performance.services

import org.apache.logging.log4j.LogManager
import org.springframework.amqp.AmqpException
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import java.lang.StringBuilder
import java.util.UUID

import ru.aps.performance.services.RatingService
import ru.aps.performance.models.Message
import ru.aps.performance.repos.MessageRepository
import ru.aps.performance.exceptions.NoSuchChatRoomException
import ru.aps.performance.exceptions.NoSuchUserInChatRoomException

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val chatRoomService: ChatRoomService,
    private val rabbitTemplate: RabbitTemplate,
    private val exchange: TopicExchange,
    private val ratingService: RatingService
) {
    fun sendMessage(message: Message) {
        try {
            rabbitTemplate.convertAndSend("${exchange.name}", message.chatRoomId.toString(), message)
            if (logger.isTraceEnabled()) {
                logger.trace("INFO: message ${message.body} sent to /queue/${message.chatRoomId}")
            }
        } catch(e: AmqpException) {
            logger.error("ERROR: " + e.message)
        }
        messageRepository.save(message)
        logger.info("Updating user rating after message by ${message.senderId} in chat room ${message.chatRoomId}")
        ratingService.updateUserRatingAfterMessage(message.chatRoomId, message.senderId)
    }

    fun getMessageHistory(receiverId: UUID, chatRoomId: UUID): List<Message> {
        if (!chatRoomService.isUserInChatRoom(chatRoomId, receiverId)) {
            throw NoSuchUserInChatRoomException("User ${receiverId} is not allowed to check history in ${chatRoomId}")
        }
        return messageRepository.findAllByChatRoomId(chatRoomId)
    }

    companion object {
        val logger = LogManager.getLogger()
    }
}