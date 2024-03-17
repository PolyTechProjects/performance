package ru.aps.performance.models

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Enumerated
import jakarta.persistence.EnumType

@Entity
@Table(name="Chats")
data class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    var name: String?,

    @Enumerated(EnumType.STRING)
    var type: ChatType
)

enum class ChatType {
    PERSONAL, GROUP
}
