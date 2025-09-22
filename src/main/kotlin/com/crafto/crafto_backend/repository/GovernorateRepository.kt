package com.crafto.crafto_backend.repository


import com.crafto.crafto_backend.entity.Governorate
import org.springframework.data.mongodb.repository.MongoRepository

interface GovernorateRepository : MongoRepository<Governorate, String>