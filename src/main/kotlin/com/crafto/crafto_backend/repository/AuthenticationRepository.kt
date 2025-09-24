package com.crafto.crafto_backend.repository

import com.crafto.crafto_backend.entity.Authentication
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface AuthenticationRepository: MongoRepository<Authentication, ObjectId> {
}