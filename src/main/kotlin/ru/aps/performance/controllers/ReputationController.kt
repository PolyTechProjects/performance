package ru.aps.performance.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import ru.aps.performance.services.RatingService
import ru.aps.performance.dto.ReputationResponse
import org.springframework.beans.factory.annotation.Autowired

@RestController
class ReputationController(@Autowired private val ratingService: RatingService) {

    @GetMapping("/reputation")
    fun getReputation(@RequestParam("userId") userId: UUID): ReputationResponse {
        return ratingService.getUserReputation(userId)
    }
}
