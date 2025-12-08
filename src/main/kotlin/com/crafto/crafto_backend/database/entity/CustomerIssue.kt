package com.crafto.crafto_backend.database.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("customer_issue")
data class CustomerIssue(
    @Id
    val id: String? = null,
    val title: String,
    val description: String,
    val status: CustomerIssueStatus,
    val customerId: String,
    val categoryId: String,
    val governmentId: String,
    val districtId: String,
    val locationDetails: String,
    val photos: List<String>
)