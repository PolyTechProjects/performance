package ru.aps.performance.controllers

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import ru.aps.performance.exceptions.NoSuchChatRoomException
import ru.aps.performance.exceptions.NoSuchUserInChatRoomException

@ControllerAdvice
class BaseController() {
    @ExceptionHandler(NoSuchChatRoomException::class)
    fun noSuchChatRoomExceptionHandler(ex: NoSuchChatRoomException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoSuchUserInChatRoomException::class)
    fun noSuchUserInChatRoomExceptionHandler(ex: NoSuchUserInChatRoomException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }
}