package com.crafto.crafto_backend.database.repository

import com.crafto.crafto_backend.database.entity.District
import org.springframework.data.mongodb.repository.MongoRepository

interface DistrictRepository : MongoRepository<District, String> {
    fun findByGovernorateId(governorateId: String): List<District>
}