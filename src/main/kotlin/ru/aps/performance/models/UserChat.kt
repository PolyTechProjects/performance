package ru.aps.performance.models

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.IdClass
import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Entity
@Table(name="User_Chat")
@IdClass(UserChatId::class)
data class UserChat(
    @Id
    @Column(name="user_id")
    val userId: String,

    @Id
    @Column(name="chat_id")
    val chatId: Long
)

@Embeddable
data class UserChatId(
    val userId: String = "",
    val chatId: Long = 0
) : java.io.Serializable
