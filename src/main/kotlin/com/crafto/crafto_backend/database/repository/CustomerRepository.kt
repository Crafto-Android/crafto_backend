package com.crafto.crafto_backend.database.repository

import com.crafto.crafto_backend.database.entity.Customer
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface CustomerRepository : MongoRepository<Customer, ObjectId>{
    fun findByUserId(userId: String): Customer?
}