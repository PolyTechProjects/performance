package ru.aps.performance.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.http.MediaType
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import ru.aps.performance.models.*
import ru.aps.performance.repos.MessageRepository
import ru.aps.performance.repos.UserChatRatingRepository
import ru.aps.performance.repos.UserReputationRepository
import ru.aps.performance.repos.ChatRoomRepository
import java.io.FileInputStream
import java.util.*

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

@Service
class RatingService(
    private val chatRoomRepository: ChatRoomRepository,
    private val userChatRatingRepository: UserChatRatingRepository,
    private val userReputationRepository: UserReputationRepository,
    private val messageRepository: MessageRepository
) {
    @Value("\${user.rate.key}")
    private lateinit var apiKey: String

    fun updateUserRatingAfterMessage(chatRoomId: UUID, senderId: UUID) {

        val count = countMessagesInChatRoom(chatRoomId)
        if (count % 10 == 0) {
            val messages = getMessageHistoryForChatRoom(chatRoomId)
            val chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow { NoSuchElementException("Chat room not found!") }
            val dialogue = "${chatRoom.firstUserId} ${chatRoom.secondUserId}\n${messages.joinToString("\n") { it.toString() }}"

            val response = requestOpenAI(dialogue)
            updateRatings(chatRoomId, response)
            calculateAndSaveUserReputation(senderId)
        }
    }
    // fun updateUserRatingAfterMessage(chatRoomId: UUID, senderId: UUID) {
    //     getMessageHistoryForChatRoom(chatRoomId)
    //         .collectList()
    //         .subscribe { messages ->
    //             val messagesAsString = messages.joinToString(separator = "\n") { it.body }
    //             updateRatings(chatRoomId, messagesAsString)
    //             calculateAndSaveUserReputation(senderId)
    //         }
    // }

    // private fun requestOpenAI(dialogue: String): Mono<String> {
    //     return webClient.post()
    //         .uri("https://api.openai.com/v1/chat/completions")
    //         .header("Authorization", "Bearer $apiKey")
    //         .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
    //         .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
    //         .bodyValue(mapOf(
    //             "model" to "gpt-3.5-turbo-0125",
    //             "messages" to listOf(
    //                 mapOf("role" to "system", "content" to "You are an AI evaluator of user interactions within a messenger application, focusing on analyzing the creativity and friendliness of messages for specific user IDs listed at the beginning of the input..."),
    //                 mapOf("role" to "user", "content" to dialogue)
    //             )
    //         ))
    //         .retrieve()
    //         .bodyToMono(String::class.java)
    // }

    // fun countMessagesInChatRoom(chatRoomId: UUID): Mono<Long> {
    //     return reactiveMessageRepository.countByChatRoomId(chatRoomId)
    // }

    // fun getMessageHistoryForChatRoom(chatRoomId: UUID): Flux<Message> {
    //     return reactiveMessageRepository.findAllByChatRoomId(chatRoomId)
    //}

    fun countMessagesInChatRoom(chatRoomId: UUID): Int {
        return messageRepository.countByChatRoomId(chatRoomId)
    }

    fun getMessageHistoryForChatRoom(chatRoomId: UUID): List<Message> {
        return messageRepository.findAllByChatRoomId(chatRoomId)
    }

    private fun requestOpenAI(dialogue: String): String {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders().apply {
            add("Authorization", "Bearer $apiKey")
            add("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 YaBrowser/24.1.0.0 Safari/537.36")
        }
        val body = mapOf(
            "model" to "gpt-3.5-turbo-0125",
            "messages" to listOf(
                mapOf(
                    "role" to "system",
                    "content" to "You are an AI evaluator of user interactions within a messenger application, focusing on analyzing the creativity and friendliness of messages for specific user IDs listed at the beginning of the input..."
                ),
                mapOf(
                    "role" to "user",
                    "content" to dialogue
                )
            )
        )
        val request = HttpEntity(body, headers)
        val response = restTemplate.postForObject("https://api.openai.com/v1/chat/completions", request, String::class.java)
        return response ?: throw RuntimeException("Failed to get response from OpenAI")
    }

    private fun updateRatings(chatRoomId: UUID, jsonResponse: String) {
        val ratings = parseJsonResponse(jsonResponse) // Предположим, что этот метод парсит JSON и возвращает Map<UUID, UserChatRating>
        ratings.forEach { (userId, rating) ->
            val currentRating = userChatRatingRepository.findById(userId).orElse(UserChatRating(userId = userId, chatRoomId = chatRoomId))
            currentRating.creativity = rating.creativity
            currentRating.friendliness = rating.friendliness
            userChatRatingRepository.save(currentRating)
        }
    }
    
    private fun calculateAndSaveUserReputation(userId: UUID) {
        val ratings = userChatRatingRepository.findAllByUserId(userId)
    
        val totalMessages = ratings.map { rating ->
            countMessagesInChatRoom(rating.chatRoomId)
        }.sum().toDouble()
    
        val weightedCreativity = ratings.map { rating ->
            val messagesInChat = countMessagesInChatRoom(rating.chatRoomId)
            rating.creativity * (messagesInChat / totalMessages)
        }.sum()
    
        val weightedFriendliness = ratings.map { rating ->
            val messagesInChat = countMessagesInChatRoom(rating.chatRoomId)
            rating.friendliness * (messagesInChat / totalMessages)
        }.sum()
    
        val userReputation = userReputationRepository.findById(userId).orElse(UserReputation(userId = userId))
        userReputation.creativity = weightedCreativity
        userReputation.friendliness = weightedFriendliness
        userReputationRepository.save(userReputation)
    }

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

    data class UserChatRatingUpdate(
        val userId: UUID,
        val creativity: Double,
        val friendliness: Double
    )

    fun parseJsonResponse(jsonResponse: String): Map<UUID, UserChatRatingUpdate> {
        val mapper = jacksonObjectMapper()
        val response: OpenAIResponse = mapper.readValue(jsonResponse)
        val content = response.choices.first().message.content

        val ratings: Map<String, Map<String, Double>> = mapper.readValue(content.removePrefix("```json\n").removeSuffix("\n```"))

        return ratings.mapKeys { UUID.fromString(it.key) }.mapValues {
            UserChatRatingUpdate(
                userId = it.key,
                creativity = it.value["Creativity"] ?: 0.0,
                friendliness = it.value["Friendliness"] ?: 0.0
            )
        }
    }
}
