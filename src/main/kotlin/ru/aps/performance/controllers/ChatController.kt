package ru.aps.performance.controllers

import ru.aps.performance.models.Chat
import ru.aps.performance.models.ChatType
import ru.aps.performance.services.ChatService

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chats")
class ChatController(private val chatService: ChatService) {

    @PostMapping
    fun createChat(@RequestBody request: CreateChatRequest): ResponseEntity<Chat> {
        val chat = chatService.createChat(request.name, request.type, request.userIds)
        return ResponseEntity.ok(chat)
    }

    @GetMapping("/{userId}")
    fun getChatsForUser(@PathVariable userId: String): ResponseEntity<List<Chat>> {
        val chats = chatService.getChatsForUser(userId)
        return ResponseEntity.ok(chats)
    }

    data class CreateChatRequest(
        val name: String?,
        val type: ChatType,
        val userIds: List<String>
    )
}
