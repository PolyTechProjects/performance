package ru.aps.performance.services

import org.springframework.stereotype.Service
import ru.aps.performance.repos.ChatRoomRepository
import ru.aps.performance.models.ChatRoom
import ru.aps.performance.exceptions.NoSuchChatRoomException
import ru.aps.performance.exceptions.NoSuchUserInChatRoomException
import ru.aps.performance.exceptions.DuplicateChatRoomException
import java.util.UUID

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
) {
    fun addChatRoom(firstUserId: UUID, secondUserId: UUID): UUID {
        if (isChatRoomExists(firstUserId, secondUserId)) {
            throw DuplicateChatRoomException("ChatRoom with ${firstUserId} and ${secondUserId} users already exists")
        }
        val chatRoomId = UUID.randomUUID()
        val chatRoom = ChatRoom(chatRoomId, firstUserId, secondUserId)
        chatRoomRepository.save(chatRoom)
        return chatRoomId
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

    fun isChatRoomExists(firstUserId: UUID, secondUserId: UUID): Boolean {
        return chatRoomRepository.findChatRoomByUserIds(firstUserId, secondUserId).isPresent()
    }

    fun purgeAllChatRooms() {
        chatRoomRepository.deleteAll()
    }
}