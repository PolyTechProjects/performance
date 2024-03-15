package ru.aps.performance.services

import org.springframework.stereotype.Service

import ru.aps.performance.repos.UserRepository
import ru.aps.performance.models.User

@Service
class GeopositionService(
    private val userRepository: UserRepository
) {
    fun saveCurrentGeoposition(latitude: Double, longitude: Double, uid: String) {
        val user = userRepository.findById(uid).get()
        user.latitude = latitude
        user.longitude = longitude
        userRepository.save(user)
    }
}