package com.crafto.crafto_backend.repository

import com.crafto.crafto_backend.entity.CustomerIssue
import org.springframework.data.mongodb.repository.MongoRepository

interface CustomerIssueRepository : MongoRepository<CustomerIssue, String>