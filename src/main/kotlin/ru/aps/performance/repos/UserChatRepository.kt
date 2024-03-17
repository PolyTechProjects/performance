package ru.aps.performance.repos

import ru.aps.performance.models.UserChat
import ru.aps.performance.models.UserChatId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserChatRepository : JpaRepository<UserChat, UserChatId> {
    fun findByUserId(userId: String): List<UserChat>
}
