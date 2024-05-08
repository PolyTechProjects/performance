package ru.aps.performance.dto

data class OpenAIResponse(
        val choices: List<Choice>
    ) {
        data class Choice(
            val message: Message
        ) {
            data class Message(
                val role: String,
                val content: String
            )
        }
    }