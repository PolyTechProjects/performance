package ru.aps.performance.dto

data class ReputationResponse(
    val userId: String,
    val creativity: Double,
    val friendliness: Double
)
