package ru.aps.performance.repos

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.aps.performance.models.User

@Repository
interface UserRepository: CrudRepository<User, String> {
    @Query(
        value = "select * from users where (point(users.longitude, users.latitude)<@>point(:longitude, :latitude)) * 1609.344 <= :radius",
        nativeQuery = true
    )
    fun findUsersByGeoposition(latitude: Double, longitude: Double, radius: Int): List<User>
}