package ru.aps.performance.services

import org.apache.logging.log4j.LogManager
import org.springframework.amqp.AmqpException
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import java.lang.StringBuilder

import ru.aps.performance.models.Message
import ru.aps.performance.repos.MessageHistoryRepository
import ru.aps.performance.repos.ChatRoomRepository

@Service
class MessageService(
    private val messageHistoryRepository: MessageHistoryRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val rabbitAdmin: RabbitAdmin,
    private val rabbitTemplate: RabbitTemplate,
    private val directExchange: DirectExchange
) {
    fun subscribeToQueue(chatRoomId: String) {
        if (chatRoomRepository.findById(chatRoomId).isEmpty) {
            //throw exception on 400
            logger.warn("WARN: subscribing to queue with non existing chatRoom = ${chatRoomId}")
        }
        val queueName = "/queue/$chatRoomId"
        val queue = Queue(queueName)
        val binding = BindingBuilder.bind(queue).to(directExchange).with(chatRoomId)
        logger.info(queueName)
        logger.info(directExchange.name)
        try {
            rabbitAdmin.declareQueue(queue)
            logger.info("INFO: declared Queue")
            rabbitAdmin.declareBinding(binding)
            logger.info("INFO: declared Binding")
        } catch (e: AmqpException) {
            logger.error("ERROR: " + e.message)
        }
    }

    fun sendMessage(message: Message) {
        try {
            rabbitTemplate.convertAndSend(directExchange.name, message.chatRoomId, message.body)
            logger.info("INFO: message ${message.body} sent to /queue/${message.chatRoomId}")
        } catch(e: AmqpException) {
            logger.error("ERROR: " + e.message)
        }
        saveMessage(message)
    }

    private fun saveMessage(message: Message) {
        val messageHistory = messageHistoryRepository.findByChatRoomId(message.chatRoomId)
        val history = messageHistory.history
        val newHistory = StringBuilder(history).append(message.toString()).toString()
        messageHistory.history = newHistory
        messageHistoryRepository.save(messageHistory)
    }

    companion object {
        val logger = LogManager.getLogger()
    }
}