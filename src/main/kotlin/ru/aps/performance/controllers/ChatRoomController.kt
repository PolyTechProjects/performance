package ru.aps.performance.controllers

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import ru.aps.performance.services.ChatRoomService
import ru.aps.performance.dto.ChatRoomRequest
import ru.aps.performance.dto.ChatRoomResponse
import ru.aps.performance.exceptions.NotEnoughParticipantsException
import ru.aps.performance.exceptions.NoSuchChatRoomException
import java.util.UUID

@RestController
@RequestMapping("/chatRoom")
class ChatRoomController(
    private val chatRoomService: ChatRoomService
) {
    @PostMapping
    fun addChatRoom(@RequestBody chatRoomRequest: ChatRoomRequest): String {
        if (chatRoomRequest.secondUserId == null) {
            throw NotEnoughParticipantsException("Not enough participants to create a chat between")
        }
        val firstUserId = UUID.fromString(chatRoomRequest.firstUserId)
        val secondUserId = UUID.fromString(chatRoomRequest.secondUserId)
        val chatRoomId = chatRoomService.addChatRoom(firstUserId, secondUserId, chatRoomRequest.name)
        return chatRoomId.toString()
    }

    @DeleteMapping
    fun deleteChatRoom(@RequestBody chatRoomRequest: ChatRoomRequest) {
        if (chatRoomRequest.chatRoomId == null) {
            throw NoSuchChatRoomException("Not valid chatRoomId")
        }
        val chatRoomId = UUID.fromString(chatRoomRequest.chatRoomId)
        val firstUserId = UUID.fromString(chatRoomRequest.firstUserId)
        chatRoomService.deleteChatRoom(chatRoomId, firstUserId)
    }

    @GetMapping
    fun getChatRoom(@RequestParam chatRoomId: String, @RequestParam userId: String): ChatRoomResponse {
        val _chatRoomId = UUID.fromString(chatRoomId)
        val _userId = UUID.fromString(userId)
        val chatRoom = chatRoomService.getChatRoom(_chatRoomId, _userId)
        return ChatRoomResponse(chatRoom.uid.toString(), chatRoom.name)
    }
}