package ru.aps.performance.repos

import org.springframework.data.repository.CrudRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.Modifying
import jakarta.transaction.Transactional
import java.util.UUID

import ru.aps.performance.models.UserXChatRoom

interface UserXChatRoomRepository: CrudRepository<UserXChatRoom, Long> {
    @Query(value="SELECT * FROM user_and_chat_room WHERE chat_room_id=:chatRoomId",nativeQuery=true)
    fun findAllByChatRoomId(chatRoomId: UUID): List<UserXChatRoom>

    @Transactional
    @Modifying
    @Query(value="DELETE FROM user_and_chat_room WHERE chat_room_id=:chatRoomId",nativeQuery=true)
    fun deleteAllByChatRoomId(chatRoomId: UUID): List<UserXChatRoom>
}