package ru.aps.performance.dto

data class GeopositionRequest(
    val uid: String,
    val latitude: Double,
    val longitude: Double
)