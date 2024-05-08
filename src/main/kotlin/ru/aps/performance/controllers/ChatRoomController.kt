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
        if (chatRoomRequest.firstUserId == chatRoomRequest.secondUserId) {
            throw NotEnoughParticipantsException("You cannot create a chatroom with yourself")
        }
        val firstUserId = UUID.fromString(chatRoomRequest.firstUserId)
        val secondUserId = UUID.fromString(chatRoomRequest.secondUserId)
        val chatRoomId = chatRoomService.addChatRoom(firstUserId, secondUserId)
        return chatRoomId.toString()
    }

    @GetMapping
    fun getChatRoom(@RequestParam firstUserId: String, @RequestParam secondUserId: String): ChatRoomResponse {
        val _firstUserId = UUID.fromString(firstUserId)
        val _secondUserId = UUID.fromString(secondUserId)
        val chatRoom = chatRoomService.getChatRoom(_firstUserId, _secondUserId)
        return ChatRoomResponse(chatRoom.uid.toString())
    }

    @DeleteMapping("/all")
    fun purgeAllChatRooms() {
        chatRoomService.purgeAllChatRooms()
    }
}