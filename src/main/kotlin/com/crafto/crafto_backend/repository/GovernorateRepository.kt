// GovernorateRepository.kt
package com.crafto.crafto_backend.repository

import com.crafto.crafto_backend.entity.Governorates
import org.springframework.data.mongodb.repository.MongoRepository

interface GovernorateRepository : MongoRepository<Governorates, String>