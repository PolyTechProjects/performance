package ru.aps.performance.controllers

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping

@RestController
class PingController() {
    @GetMapping("/ping")
    fun ping(): String {
        return "ping"
    }

    @GetMapping("/")
    fun root(): String {
        return """
        <!DOCTYPE html>
        <html>
            <head>
                <title>Main Page</title>
                <script src="/js/stomp.js"></script>
            </head>
            <body>
                <h1>Test '/' Page</h1>
                <p>This is test page with imported stomp.js lib</p>
            </body>
        </html>
        """
    }
}