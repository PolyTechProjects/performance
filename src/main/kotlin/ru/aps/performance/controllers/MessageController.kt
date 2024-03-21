package ru.aps.performance.controllers

import org.springframework.stereotype.Controller
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody

import ru.aps.performance.services.MessageService
import ru.aps.performance.models.Message
import ru.aps.performance.dto.MessageRequest
import ru.aps.performance.dto.MessageHistoryResponse
import ru.aps.performance.dto.MessageHistoryRequest
import java.util.UUID

@Controller
class MessageController(
    private val messageService: MessageService
) {
    @MessageMapping("/send")
    fun sendMessage(messageRequest: MessageRequest) {
        val chatRoomId = UUID.fromString(messageRequest.chatRoomId)
        val senderId = UUID.fromString(messageRequest.senderId)
        val message = Message(
            chatRoomId=chatRoomId,
            senderId=senderId,
            body=messageRequest.body,
            sendTime=messageRequest.sendTime
        )
        messageService.sendMessage(message)
    }

    @GetMapping("/history")
    fun getMessageHistory(@RequestBody messageHistoryRequest: MessageHistoryRequest): MessageHistoryResponse {
        val receiverId = UUID.fromString(messageHistoryRequest.receiverId)
        val chatRoomId = UUID.fromString(messageHistoryRequest.chatRoomId)
        return MessageHistoryResponse(messageService.getMessageHistory(receiverId, chatRoomId))
    }
}