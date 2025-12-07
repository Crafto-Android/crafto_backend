package com.crafto.crafto_backend.database.repository

import com.crafto.crafto_backend.database.entity.CustomerIssue
import com.crafto.crafto_backend.database.entity.CustomerIssueStatus
import org.springframework.data.mongodb.repository.MongoRepository

interface CustomerIssueRepository : MongoRepository<CustomerIssue, String>{
    fun findByCustomerId(customerId: String): List<CustomerIssue>
    fun findByStatus(status: CustomerIssueStatus): List<CustomerIssue>

}