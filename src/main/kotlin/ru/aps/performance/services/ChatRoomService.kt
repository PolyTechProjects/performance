package ru.aps.performance.services

import org.springframework.stereotype.Service
import ru.aps.performance.repos.ChatRoomRepository
import ru.aps.performance.repos.UserXChatRoomRepository
import ru.aps.performance.models.ChatRoom
import ru.aps.performance.models.UserXChatRoom
import ru.aps.performance.exceptions.NoSuchChatRoomException
import ru.aps.performance.exceptions.NoSuchUserInChatRoomException
import java.util.UUID

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val userXChatRoomRepository: UserXChatRoomRepository
) {
    fun addChatRoom(firstId: UUID, secondId: UUID, name: String?) {
        val chatRoomId = UUID.randomUUID()
        val chatRoom = ChatRoom(chatRoomId, name)
        chatRoomRepository.save(chatRoom)
        userXChatRoomRepository.save(UserXChatRoom(userId=firstId, chatRoomId=chatRoomId))
        userXChatRoomRepository.save(UserXChatRoom(userId=secondId, chatRoomId=chatRoomId))
    }

    fun deleteChatRoom(chatRoomId: UUID, userId: UUID) {
        if (!isUserInChatRoom(chatRoomId, userId)) {
            throw NoSuchUserInChatRoomException("User ${userId} is not allowed to delete ${chatRoomId} chat room")
        }
        chatRoomRepository.deleteById(chatRoomId)
        userXChatRoomRepository.deleteAllByChatRoomId(chatRoomId)
    }

    fun getChatRoom(chatRoomId: UUID, userId: UUID): ChatRoom {
        if (!isUserInChatRoom(chatRoomId, userId)) {
            throw NoSuchUserInChatRoomException("User ${userId} is not allowed to gain access to ${chatRoomId} chat room")
        }
        val chatRoom = chatRoomRepository.findById(chatRoomId)
        if (chatRoom.isEmpty()) {
            throw NoSuchChatRoomException("There is no ChatRoom with ${chatRoomId} id");
        }
        return chatRoom.get();
    }

    fun isUserInChatRoom(chatRoomId: UUID, userId: UUID): Boolean {
        val usersInChatRoom = userXChatRoomRepository.findAllByChatRoomId(chatRoomId).map { it.userId };
        return !usersInChatRoom.contains(userId);
    }
}