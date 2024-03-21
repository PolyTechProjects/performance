package ru.aps.performance.dto

import java.util.UUID
import ru.aps.performance.models.Message

class MessageHistoryResponse(
    val history: List<Message>
)