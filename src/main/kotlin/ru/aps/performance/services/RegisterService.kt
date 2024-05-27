package ru.aps.performance.services

import org.springframework.stereotype.Service
import org.springframework.dao.DataAccessException

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
        val userOpt = usersService.findUserByName(name)
        if (userOpt.isPresent()) {
            throw DuplicateUserException("User $name already exists")
        }
        val userId = UUID.randomUUID()
        val user = User(userId, name, password)
        try {
            userRepository.save(user)
        }
        catch (ex: DataAccessException) {
            throw DuplicateUserException("Failed to save user $user. ${ex.message}")
        }
        return userId
    }
}