package ru.aps.performance.repos

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

import ru.aps.performance.models.ChatRoom

@Repository
interface ChatRoomRepository: CrudRepository<ChatRoom, String>