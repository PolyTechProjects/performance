package ru.aps.performance.models

import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue

@Entity
@Table(name = "chatrooms")
data class ChatRoom(
    @Id
    @GeneratedValue
    val uid: String
)