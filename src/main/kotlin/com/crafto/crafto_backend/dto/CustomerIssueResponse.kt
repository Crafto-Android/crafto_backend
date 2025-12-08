package com.crafto.crafto_backend.dto

import com.crafto.crafto_backend.database.entity.CustomerIssueStatus

data class CustomerIssueResponse(
    val id: String,
    val title: String,
    val description: String,
    val status: CustomerIssueStatus,
    val categoryId: String,
    val customerId: String,
    val governmentId: String,
    val districtId: String,
    val locationDetails: String,
    val photos: List<String>
)