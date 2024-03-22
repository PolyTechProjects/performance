package ru.aps.performance.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody

import ru.aps.performance.dto.RegisterRequest
import ru.aps.performance.models.User
import ru.aps.performance.services.RegisterService
import java.util.UUID

@RestController
class RegisterController(private val registerService: RegisterService) {
    @PostMapping("/register")
    fun registerUser(@RequestBody registerRequest: RegisterRequest): String {
        val userId = registerService.registerUser(registerRequest.name, registerRequest.password)
        return userId.toString()
    }
}
