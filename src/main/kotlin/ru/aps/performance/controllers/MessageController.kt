package ru.aps.performance.controllers

import org.springframework.stereotype.Controller
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestParam

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
        val message = Message.fromRequest(messageRequest)
        messageService.sendMessage(message)
    }

    @GetMapping("/history")
    @ResponseBody
    fun getMessageHistory(@RequestParam chatRoomId: String, @RequestParam receiverId: String): MessageHistoryResponse {
        val _receiverId = UUID.fromString(receiverId)
        val _chatRoomId = UUID.fromString(chatRoomId)
        return MessageHistoryResponse(messageService.getMessageHistory(_receiverId, _chatRoomId).map { it.toResponse() })
    }
}