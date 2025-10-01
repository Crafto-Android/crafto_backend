package com.crafto.crafto_backend.database.repository

import com.crafto.crafto_backend.database.entity.Craftsman
import com.crafto.crafto_backend.database.entity.CraftsmanStatus
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CraftsmanRepository : MongoRepository<Craftsman, ObjectId>{
    fun findByUserId(userId: String): Craftsman?
    fun existsByUserId(userId: String): Boolean
    fun findByStatus(status: CraftsmanStatus): List<Craftsman>
    fun findByCategoriesContaining(category: String): List<Craftsman>
}