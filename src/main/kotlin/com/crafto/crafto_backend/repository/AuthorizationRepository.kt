package com.crafto.crafto_backend.repository

import com.crafto.crafto_backend.entity.Authorization
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface AuthorizationRepository: MongoRepository<Authorization, ObjectId> {
}