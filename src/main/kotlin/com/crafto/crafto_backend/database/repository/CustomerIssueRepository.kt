package com.crafto.crafto_backend.database.repository

import com.crafto.crafto_backend.database.entity.CustomerIssue
import org.springframework.data.mongodb.repository.MongoRepository

interface CustomerIssueRepository : MongoRepository<CustomerIssue, String>{
    fun findByCustomerId(customerId: String): List<CustomerIssue>
}