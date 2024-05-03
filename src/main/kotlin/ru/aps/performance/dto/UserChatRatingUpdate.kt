package ru.aps.performance.dto

import java.util.UUID

data class UserChatRatingUpdate(
        val userId: UUID,
        val creativity: Double,
        val friendliness: Double
    )