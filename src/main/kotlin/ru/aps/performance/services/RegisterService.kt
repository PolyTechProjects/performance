package ru.aps.performance.services

import org.springframework.stereotype.Service

import ru.aps.performance.models.User
import ru.aps.performance.repos.UserRepository
import ru.aps.performance.exceptions.DuplicateUserException
import java.util.UUID

@Service
class RegisterService(
    private val usersService: UsersService,
    private val userRepository: UserRepository
) {
    fun registerUser(name: String, password: String): UUID {
        val userOpt = usersService.findUserByCredentials(name, password)
        if (userOpt.isPresent()) {
            return userOpt.get().uid
        }
        val userId = UUID.randomUUID()
        val user = User(userId, name, password)
        userRepository.save(user)
        return userId
    }
}