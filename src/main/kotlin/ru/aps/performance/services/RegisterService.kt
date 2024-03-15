package ru.aps.performance.services

import org.springframework.stereotype.Service

import ru.aps.performance.models.User
import ru.aps.performance.repos.UserRepository

@Service
class RegisterService(
    private val userRepository: UserRepository
) {
    fun registerUser(user: User) {
        userRepository.save(user)
    }
}