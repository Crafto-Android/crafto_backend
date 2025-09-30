package com.crafto.crafto_backend.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("customer_problem")
data class CustomerIssue(
    @Id
    val id: String? = null,
    val customerId: String,
    val categoryId: String,
    val issueTitle: String,
    val issueContent: String,
    val governmentId: String,
    val governmentName: String,
    val districtId: String,
    val districtName: String,
    val locationDetails: String,
    val photos: List<String>
)