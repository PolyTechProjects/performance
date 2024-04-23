package ru.aps.performance.services

import org.springframework.stereotype.Service
import ru.aps.performance.repos.ChatRoomRepository
import ru.aps.performance.models.ChatRoom
import ru.aps.performance.exceptions.NoSuchChatRoomException
import ru.aps.performance.exceptions.NoSuchUserInChatRoomException
import java.util.UUID

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
) {
    fun addChatRoom(firstId: UUID, secondId: UUID, name: String?): UUID {
        val chatRoomId = UUID.randomUUID()
        val chatRoom = ChatRoom(chatRoomId, firstId, secondId, name)
        chatRoomRepository.save(chatRoom)
        return chatRoomId
    }

    fun deleteChatRoom(chatRoomId: UUID, userId: UUID) {
        if (!isUserInChatRoom(chatRoomId, userId)) {
            throw NoSuchUserInChatRoomException("User ${userId} is not allowed to delete ${chatRoomId} chat room")
        }
        chatRoomRepository.deleteById(chatRoomId)
    }

    fun getChatRoom(firstUserId: UUID, secondUserId: UUID): ChatRoom {
        val chatRoom = chatRoomRepository.findChatRoomByUserIds(firstUserId, secondUserId)
        if (chatRoom.isEmpty()) {
            throw NoSuchChatRoomException("There is no ChatRoom with ${firstUserId} and ${secondUserId} users");
        }
        return chatRoom.get();
    }

    fun isUserInChatRoom(chatRoomId: UUID, userId: UUID): Boolean {
        val chatRoom = chatRoomRepository.findById(chatRoomId);
        if (chatRoom.isEmpty()) {
            throw NoSuchChatRoomException("There is no ChatRoom with ${chatRoomId} id");
        }
        return chatRoom.get().firstUserId == userId || chatRoom.get().secondUserId == userId
    }
}