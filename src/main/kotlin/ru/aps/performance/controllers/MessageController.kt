package ru.aps.performance.controllers

import ru.aps.performance.models.Message
import ru.aps.performance.services.MessageService

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/messages")
class MessageController(private val messageService: MessageService) {

    @PostMapping
    fun sendMessage(@RequestBody request: SendMessageRequest): ResponseEntity<Message> {
        val message = messageService.sendMessage(request.chatId, request.senderId, request.content)
        return ResponseEntity.ok(message)
    }

    @GetMapping("/{chatId}")
    fun getMessagesForChat(@PathVariable chatId: Long): ResponseEntity<List<Message>> {
        val messages = messageService.getMessagesForChat(chatId)
        return ResponseEntity.ok(messages)
    }

    data class SendMessageRequest(
        val chatId: Long,
        val senderId: String,
        val content: String
    )
}
