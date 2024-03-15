package ru.aps.performance.models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Column
import jakarta.annotation.Nullable

@Entity
@Table(name="Users")
data class User(
    @Id
    val uid: String,
    val name: String,
    val password: String,
    @Column(nullable = true)
    var latitude: Double,
    @Column(nullable = true)
    var longitude: Double
)