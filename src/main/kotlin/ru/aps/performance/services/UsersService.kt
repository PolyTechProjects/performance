package ru.aps.performance.services

import ru.aps.performance.repos.UserRepository
import ru.aps.performance.models.User
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value

@Service
class UsersService(
    private val userRepository: UserRepository,
    @Value("\${geo.radius}")
    val radius: Int
) {

    fun findAllUsers(): List<User> {
        return userRepository.findAll().toList()
    }

    fun findUsersByGeoposition(uid: String): List<User> {
        val user = userRepository.findById(uid).get()
        return userRepository.findUsersByGeoposition(user.latitude, user.longitude, radius)
    }
}