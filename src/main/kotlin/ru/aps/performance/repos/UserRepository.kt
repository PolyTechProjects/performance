package ru.aps.performance.repos

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.aps.performance.models.User
import java.util.Optional
import java.util.UUID

@Repository
interface UserRepository: CrudRepository<User, UUID> {

    @Query(
        value = "SELECT * FROM users WHERE name = :name AND password = :password",
        nativeQuery=true
    )
    fun findUserByNameAndPassword(name: String, password: String): Optional<User>
}