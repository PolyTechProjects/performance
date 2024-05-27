package ru.aps.performance.controllers

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import ru.aps.performance.exceptions.NoSuchChatRoomException
import ru.aps.performance.exceptions.NoSuchUserInChatRoomException
import ru.aps.performance.exceptions.NotEnoughParticipantsException
import ru.aps.performance.exceptions.DuplicateChatRoomException
import ru.aps.performance.exceptions.DuplicateUserException

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

    @ExceptionHandler(NotEnoughParticipantsException::class)
    fun notEnoughParticipantsExceptionHandler(ex: NotEnoughParticipantsException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DuplicateChatRoomException::class)
    fun duplicateChatRoomExceptionHandler(ex: DuplicateChatRoomException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(DuplicateUserException::class)
    fun duplicateUserExceptionHandler(ex: DuplicateUserException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
    }
}