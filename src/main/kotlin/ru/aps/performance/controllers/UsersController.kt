package ru.aps.performance.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import ru.aps.performance.services.UsersService
import ru.aps.performance.dto.UsersResponse
import java.util.UUID

@RestController
@RequestMapping("/users")
class UsersController(
    private val usersService: UsersService
) {
    @GetMapping("/all")
    fun getAllUsers(): List<UsersResponse> {
        return usersService.findAllUsers().map { UsersResponse(it.uid.toString(), it.name) }
    }
}