package ru.aps.performance.models

import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Column

@Entity
@Table(name="messagehistory")
data class MessageHistory(
    @Id
    @GeneratedValue
    val uid: Long,

    @Column(unique=true)
    val chatRoomId: String,
    
    val history: String
)