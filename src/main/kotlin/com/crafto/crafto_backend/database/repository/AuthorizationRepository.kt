package com.crafto.crafto_backend.database.repository

import com.crafto.crafto_backend.database.entity.Authorization
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface AuthorizationRepository: MongoRepository<Authorization, ObjectId> {
}