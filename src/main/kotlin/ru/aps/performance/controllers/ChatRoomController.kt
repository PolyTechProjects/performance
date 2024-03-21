package ru.aps.performance.controllers

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.GetMapping
import ru.aps.performance.services.ChatRoomService
import ru.aps.performance.dto.ChatRoomRequest
import java.util.UUID

@RestController
class ChatRoomController(
    private val chatRoomService: ChatRoomService
) {
    @PostMapping("/chatRoom")
    fun addChatRoom(@RequestBody chatRoomRequest: ChatRoomRequest) {
        val firstUserId = UUID.fromString(chatRoomRequest.firstUserId)
        val secondUserId = UUID.fromString(chatRoomRequest.secondUserId)
        chatRoomService.addChatRoom(firstUserId, secondUserId, chatRoomRequest.name)
    }

    @DeleteMapping("/chatRoom")
    fun deleteChatRoom(@RequestBody chatRoomRequest: ChatRoomRequest) {
        val chatRoomId = UUID.fromString(chatRoomRequest.chatRoomId)
        val firstUserId = UUID.fromString(chatRoomRequest.firstUserId)
        chatRoomService.deleteChatRoom(chatRoomId, firstUserId)
    }

    @GetMapping("/chatRoom")
    fun getChatRoom(@RequestBody chatRoomRequest: ChatRoomRequest) {
        val chatRoomId = UUID.fromString(chatRoomRequest.chatRoomId)
        val firstUserId = UUID.fromString(chatRoomRequest.firstUserId)
        chatRoomService.getChatRoom(chatRoomId, firstUserId)
    }
}