package ru.aps.performance.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import kotlin.random.Random

@RestController
class ReputationController {

    @GetMapping("/reputation")
    fun getReputation(@RequestParam("userId") userId: UUID): Map<String, Any> {
        val creativity = String.format("%.1f", Random.nextDouble(0.0, 5.0)).toDouble()
        val friendliness = String.format("%.1f", Random.nextDouble(0.0, 5.0)).toDouble()
        return mapOf(
            "userId" to userId.toString(),
            "creativity" to creativity,
            "friendliness" to friendliness
        )
    }
}
