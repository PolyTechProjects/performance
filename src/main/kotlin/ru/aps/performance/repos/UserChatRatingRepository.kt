package ru.aps.performance.repositories

import org.springframework.data.repository.CrudRepository
import ru.aps.performance.models.UserChatRating
import org.springframework.stereotype.Repository

import java.util.UUID
import java.util.Optional

@Repository
interface UserChatRatingRepository : CrudRepository<UserChatRating, UUID> {
    fun findAllByUserId(userId: UUID): List<UserChatRating>
    fun findByChatRoomIdAndUserId(chatRoomId: UUID, userId: UUID): Optional<UserChatRating>
}
