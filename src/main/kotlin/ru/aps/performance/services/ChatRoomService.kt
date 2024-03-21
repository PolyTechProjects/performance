package ru.aps.performance.services

import org.springframework.stereotype.Service
import ru.aps.performance.repos.ChatRoomRepository
import ru.aps.performance.repos.UserXChatRoomRepository
import ru.aps.performance.repos.MessageHistoryRepository
import ru.aps.performance.models.ChatRoom
import ru.aps.performance.models.UserXChatRoom
import ru.aps.performance.models.MessageHistory
import ru.aps.performance.exceptions.NoSuchChatRoomException
import ru.aps.performance.exceptions.NoSuchUserInChatRoomException
import java.util.UUID

@Service
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val userXChatRoomRepository: UserXChatRoomRepository,
    private val messageHistoryRepository: MessageHistoryRepository
) {
    fun addChatRoom(firstId: UUID, secondId: UUID, name: String?) {
        val chatRoomId = UUID.randomUUID()
        val chatRoom = ChatRoom(chatRoomId, name)
        chatRoomRepository.save(chatRoom)
        userXChatRoomRepository.save(UserXChatRoom(userId=firstId, chatRoomId=chatRoomId))
        userXChatRoomRepository.save(UserXChatRoom(userId=secondId, chatRoomId=chatRoomId))
    }

    fun deleteChatRoom(chatRoomId: UUID, userUid: UUID) {
        val usersInChatRoom = userXChatRoomRepository.findAllByChatRoomId(chatRoomId).map { it.userId };
        if (!usersInChatRoom.contains(userUid)) {
            throw NoSuchUserInChatRoomException("No user ${userUid} in ${chatRoomId} chatRoom")
        }
        chatRoomRepository.deleteById(chatRoomId)
        userXChatRoomRepository.deleteAllByChatRoomId(chatRoomId)
    }

    fun getChatRoom(chatRoomId: UUID, userId: UUID): ChatRoom {
        val chatRoom = chatRoomRepository.findById(chatRoomId)
        if (chatRoom.isEmpty()) {
            throw NoSuchChatRoomException("There is no ChatRoom with ${chatRoomId} id");
        }
        val usersInChatRoom = userXChatRoomRepository.findAllByChatRoomId(chatRoomId).map { it.userId };
        if (!usersInChatRoom.contains(userId)) {
            throw NoSuchUserInChatRoomException("No user ${userId} in ${chatRoomId} chatRoom")
        }
        return chatRoom.get();
    }
}