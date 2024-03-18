package ru.aps.performance.controllers

import org.springframework.stereotype.Controller
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.simp.annotation.SubscribeMapping

import ru.aps.performance.services.MessageService
import ru.aps.performance.models.Message
import ru.aps.performance.dto.MessageRequest

@Controller
class MessageController(
    private val messageService: MessageService
) {
    @SubscribeMapping("/queue/{chatRoomId}")
    fun subscribeToQueue(@DestinationVariable chatRoomId: String) {
        messageService.subscribeToQueue(chatRoomId)
    }

    @MessageMapping("/send")
    fun sendMessage(messageRequest: MessageRequest) {
        val message = Message(messageRequest.chatRoomId, messageRequest.senderId, messageRequest.body, messageRequest.sendTime)
        messageService.sendMessage(message)
    }
}