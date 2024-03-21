package ru.aps.performance.repos

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

import ru.aps.performance.models.ChatRoom
import java.util.UUID

@Repository
interface ChatRoomRepository: CrudRepository<ChatRoom, UUID>