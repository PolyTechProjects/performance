package ru.aps.performance.repos

import org.springframework.data.repository.CrudRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

import ru.aps.performance.models.ChatRoom
import java.util.UUID
import java.util.Optional

@Repository
interface ChatRoomRepository: CrudRepository<ChatRoom, UUID> {

    @Query(
        value = "SELECT * FROM chat_rooms WHERE first_user_id = :firstUserId AND second_user_id = :secondUserId OR first_user_id = :secondUserId AND second_user_id = :firstUserId",
        nativeQuery=true
    )
    fun findChatRoomByUserIds(firstUserId: UUID, secondUserId: UUID): Optional<ChatRoom>
}