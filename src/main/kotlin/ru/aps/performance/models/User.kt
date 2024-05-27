package ru.aps.performance.models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.annotation.Nullable
import java.util.UUID

@Entity
@Table(name="Users")
open class User(
    @Id
    open var uid: UUID,
    @Column(unique=true)
    open var name: String,
    open var password: String
)