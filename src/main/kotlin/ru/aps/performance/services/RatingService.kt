package ru.aps.performance.services

import org.apache.logging.log4j.LogManager
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
import ru.aps.performance.exceptions.NoSuchChatRoomException
import ru.aps.performance.dto.OpenAIResponse
import ru.aps.performance.dto.UserChatRatingUpdate
import ru.aps.performance.dto.ReputationResponse
import java.io.FileInputStream
import java.util.*

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.databind.DeserializationFeature

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
        logger.info("Starting to update user rating for message in chat room $chatRoomId by user $senderId")
        val count = countMessagesInChatRoom(chatRoomId)
        if (count % 1 == 0) {
            logger.debug("Fetching message history for chat room $chatRoomId")
            val messages = getMessageHistoryForChatRoom(chatRoomId)
            val chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow {
                NoSuchChatRoomException("Chat room not found!")
            }
            val dialogue = "${chatRoom.firstUserId} ${chatRoom.secondUserId}\n${messages.joinToString("\n") { it.toString() }}"
            logger.debug("Requesting OpenAI for ratings update")
            val response = requestOpenAI(dialogue)
            logger.debug("Updating ratings based on OpenAI response")
            updateRatings(chatRoomId, response)
            logger.info("Calculating and saving new reputation for user $senderId")
            calculateAndSaveUserReputation(senderId)
        }
    }

    fun countMessagesInChatRoom(chatRoomId: UUID): Int {
        logger.debug("Counting messages in chat room $chatRoomId")
        return messageRepository.countByChatRoomId(chatRoomId)
    }

    fun getMessageHistoryForChatRoom(chatRoomId: UUID): List<Message> {
        logger.debug("Retrieving message history for chat room $chatRoomId")
        return messageRepository.findAllByChatRoomId(chatRoomId)
    }

    fun getUserReputation(userId: UUID): ReputationResponse {
        val userReputation = userReputationRepository.findByUserId(userId).orElseThrow {
            NoSuchElementException("No reputation found for user $userId")
        }
        return ReputationResponse(
            userId = userId.toString(),
            creativity = userReputation.creativity,
            friendliness = userReputation.friendliness
        )
    } 

    private fun requestOpenAI(dialogue: String): String {
        logger.info("Requesting analysis from OpenAI for dialogue assessment")
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
                    "content" to "You are an AI evaluator of user interactions within a messenger application, focusing on analyzing the creativity and friendliness of messages for specific user IDs listed at the beginning of the input. For each message:\n- Assess creativity considering factors like originality, expressiveness, and novelty.\n- Evaluate friendliness by analyzing the tone, politeness, and warmth.\nProvide a comprehensive evaluation by listing 'Creativity: [rating]' and 'Friendliness: [rating]' next to each user ID. Ratings should range from 0 to 5, allowing one decimal place for precision. Send the response in the form of json"
                    ),
                mapOf(
                    "role" to "user",
                    "content" to dialogue
                )
            )
        )
        val request = HttpEntity(body, headers)
        try {
            val response = restTemplate.postForObject("https://api.openai.com/v1/chat/completions", request, String::class.java)
            logger.info("Received response from OpenAI successfully")
            return response ?: throw RuntimeException("Failed to get response from OpenAI")
        } catch (e: Exception) {
            logger.error("Failed to communicate with OpenAI: ${e.message}")
            throw e
        }
    }

    private fun updateRatings(chatRoomId: UUID, jsonResponse: String) {
        logger.debug("Parsing JSON response and updating ratings in the database")
        val ratings = parseJsonResponse(jsonResponse)
        ratings.forEach { (userId, rating) ->
            val currentRatingOpt = userChatRatingRepository.findByChatRoomIdAndUserId(chatRoomId, userId)
            val currentRating = currentRatingOpt.orElseGet {
                UserChatRating(
                    chatRoomId = chatRoomId,
                    userId = userId
                )
            }

            currentRating.creativity = rating.creativity
            currentRating.friendliness = rating.friendliness
            userChatRatingRepository.save(currentRating)
    
            if (currentRatingOpt.isPresent) {
                logger.debug("Updated ratings for user $userId in chat room $chatRoomId")
            } else {
                logger.debug("Created and saved new rating for user $userId in chat room $chatRoomId")
            }
        }
    }
    
    private fun calculateAndSaveUserReputation(userId: UUID) {
        logger.debug("Calculating and saving user reputation for user $userId")
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
        logger.info("Saved new reputation for user $userId")
    }

    fun parseJsonResponse(jsonResponse: String): Map<UUID, UserChatRatingUpdate> {
        logger.debug("Starting to parse JSON response from OpenAI")
        val mapper = jacksonObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

        try {
            val response: OpenAIResponse = mapper.readValue(jsonResponse)
            val content = response.choices.first().message.content
            logger.debug("Extracted content from JSON response for parsing ratings")

            val cleanContent = content.removePrefix("```json\n").removeSuffix("\n```")
            val ratings: Map<String, Map<String, Double>> = mapper.readValue(cleanContent)

            logger.info("Successfully parsed ratings from JSON")
            return ratings.mapKeys { UUID.fromString(it.key) }.mapValues {
                UserChatRatingUpdate(
                    userId = it.key,
                    creativity = it.value["Creativity"] ?: 0.0,
                    friendliness = it.value["Friendliness"] ?: 0.0
                )
            }
        } catch (e: Exception) {
            logger.error("Failed to parse JSON response: ${e.message}")
            throw RuntimeException("Error parsing JSON response: ${e.message}", e)
        }
    }

    companion object {
        val logger = LogManager.getLogger()
    }
}
