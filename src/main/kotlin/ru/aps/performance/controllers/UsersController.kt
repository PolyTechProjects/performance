package ru.aps.performance.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import ru.aps.performance.services.UsersService
import ru.aps.performance.dto.UsersResponse
import ru.aps.performance.dto.GeopositionRequest
import ru.aps.performance.dto.UsersRequest

@RestController
@RequestMapping("/users")
class UsersController(
    private val usersService: UsersService
) {
    @GetMapping("/all")
    fun getAllUsers(): List<UsersResponse> {
        return usersService.findAllUsers().map { UsersResponse(it.uid, it.name) }
    }

    @GetMapping("/geo")
    fun getUsersByGeoposition(@RequestBody usersRequest: UsersRequest): List<UsersResponse> {
        val users = usersService.findUsersByGeoposition(usersRequest.uid)
        return users.map { UsersResponse(it.uid, it.name) }.filter { it.uid != usersRequest.uid }
    }
}