package ru.aps.performance.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.data.geo.Point

import ru.aps.performance.services.GeopositionService
import ru.aps.performance.dto.GeopositionRequest
import ru.aps.performance.dto.GeopositionResponse

@RestController
class GeopositionController(private val geopositionService: GeopositionService) {
    @PostMapping("/save")
    fun saveCurrentGeoposition(@RequestBody geopositionRequest: GeopositionRequest) {
        geopositionService.saveCurrentGeoposition(geopositionRequest.latitude, geopositionRequest.longitude, geopositionRequest.uid)
    }
}