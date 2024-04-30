package ru.aps.performance.repositories

import org.springframework.data.jpa.repository.JpaRepository
import ru.aps.performance.models.UserReputation
import java.util.UUID
import java.util.Optional

interface UserReputationRepository : JpaRepository<UserReputation, UUID> {
    fun findByUserId(userId: UUID): Optional<UserReputation>
}
