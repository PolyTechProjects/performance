package ru.aps.performance.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.aps.performance.services.UsersService
import ru.aps.performance.dto.UsersResponse
import java.util.UUID

@RestController
@RequestMapping("/users")
class UsersController(
    private val usersService: UsersService
) {
    @GetMapping("/all")
    fun getAllUsersExceptMe(@RequestParam userId: String): List<UsersResponse> {
        val _userId = UUID.fromString(userId)
        return usersService.findAllUsersExceptMe(_userId).map { UsersResponse(it.uid.toString(), it.name) }
    }

    @DeleteMapping("/all")
    fun purgeAllUsers() {
        usersService.purgeAllUsers()
    }
}