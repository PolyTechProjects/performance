package ru.aps.performance.repos

import org.springframework.data.repository.CrudRepository
import ru.aps.performance.models.UserReputation
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.Optional

@Repository
interface UserReputationRepository : CrudRepository<UserReputation, UUID> {
    fun findByUserId(userId: UUID): Optional<UserReputation>
}
