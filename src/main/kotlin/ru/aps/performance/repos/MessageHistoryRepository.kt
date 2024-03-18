package ru.aps.performance.repos

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

import ru.aps.performance.models.MessageHistory

@Repository
interface MessageHistoryRepository: CrudRepository<MessageHistory, Long> {
    @Query(
        value = "select * from messagehistory where chat_room_id=:chatRoomId",
        nativeQuery=true
    )
    fun findByChatRoomId(chatRoomId: String): MessageHistory?

    @Query(
        value = "update messagehistory set history=:history where uid=:uid",
        nativeQuery=true
    )
    fun updateHistoryById(uid: Long, history: String)
}