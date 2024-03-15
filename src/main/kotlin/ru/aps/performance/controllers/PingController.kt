package ru.aps.performance.controllers

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping

@RestController
class PingController() {
    @GetMapping("/ping")
    fun ping(): String {
        return "ping"
    }
}