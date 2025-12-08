package com.crafto.crafto_backend.database.repository

import com.crafto.crafto_backend.database.entity.Governorates
import org.springframework.data.mongodb.repository.MongoRepository

interface GovernorateRepository : MongoRepository<Governorates, String>