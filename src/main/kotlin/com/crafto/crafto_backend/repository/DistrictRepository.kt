package com.crafto.crafto_backend.repository

import com.crafto.crafto_backend.entity.District
import org.springframework.data.mongodb.repository.MongoRepository

interface DistrictRepository : MongoRepository<District, String> {
    fun findByGovernorateId(governorateId: String): List<District>
}