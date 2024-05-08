package ru.aps.performance.models

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import java.util.UUID

@Entity
@Table(name = "user_reputation")
class UserReputation(
    @Id
    var userId: UUID,

    var creativity: Double = 0.0,
    var friendliness: Double = 0.0
)
