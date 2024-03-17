package ru.aps.performance.services

import ru.aps.performance.models.Chat
import ru.aps.performance.models.ChatType
import ru.aps.performance.models.UserChat
import ru.aps.performance.repos.ChatRepository
import ru.aps.performance.repos.UserChatRepository

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val userChatRepository: UserChatRepository
) {

    @Transactional
    fun createChat(name: String?, type: ChatType, userIds: List<String>): Chat {
        val chat = chatRepository.save(Chat(name = name, type = type))

        userIds.forEach { userId ->
            userChatRepository.save(UserChat(userId = userId, chatId = chat.id))
        }

        return chat
    }

    fun getChatsForUser(userId: String): List<Chat> {
        return userChatRepository.findByUserId(userId).map { userChat ->
            chatRepository.findById(userChat.chatId).get()
        }
    }
}
