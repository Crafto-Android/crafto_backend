package com.crafto.crafto_backend.database.repository

import com.crafto.crafto_backend.database.entity.Customer
import org.springframework.data.mongodb.repository.MongoRepository

interface CustomerRepository : MongoRepository<Customer, String>