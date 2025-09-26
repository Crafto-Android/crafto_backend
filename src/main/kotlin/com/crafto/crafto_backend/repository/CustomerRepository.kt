package com.crafto.crafto_backend.repository

import com.crafto.crafto_backend.entity.Customer
import org.springframework.data.mongodb.repository.MongoRepository

interface CustomerRepository : MongoRepository<Customer, String>