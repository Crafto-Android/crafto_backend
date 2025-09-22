package com.crafto.crafto_backend.controller


import com.crafto.crafto_backend.dto.DistrictResponse
import com.crafto.crafto_backend.dto.GovernorateResponse
import com.crafto.crafto_backend.service.LocationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/location")
class LocationController(private val service: LocationService) {

    @GetMapping("/governorates")
    fun getAllGovernorates(): List<GovernorateResponse> {
        return service.getAllGovernorates()
    }

    @GetMapping("/areas/{governorateId}")
    fun getDistrictByGovernorate(@PathVariable governorateId: String): List<DistrictResponse> {
        return service.getDistrictByGovernorate(governorateId)
    }
}