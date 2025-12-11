package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.database.repository.DistrictRepository
import com.crafto.crafto_backend.database.repository.GovernorateRepository
import com.crafto.crafto_backend.dto.response.DistrictResponse
import com.crafto.crafto_backend.dto.response.GovernorateResponse
import com.crafto.crafto_backend.mapper.toAreaResponse
import com.crafto.crafto_backend.mapper.toGovernorateResponse
import org.springframework.stereotype.Service
import kotlin.collections.map

@Service
class LocationService(
    private val governorateRepository: GovernorateRepository,
    private val districtRepository: DistrictRepository
) {
    fun getAllGovernorates(): List<GovernorateResponse> {
        return governorateRepository.findAll().map { it.toGovernorateResponse() }
    }

    fun getDistrictByGovernorate(governorateId: String): List<DistrictResponse> {
        val optionalGov = governorateRepository.findById(governorateId)
        if (optionalGov.isEmpty) {
            return emptyList()
        }
        val districts = districtRepository.findByGovernorateId(governorateId)
        return districts.map { it.toAreaResponse() }
    }
}