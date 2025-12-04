package com.crafto.crafto_backend.response

import com.crafto.crafto_backend.entity.CustomerIssueStatus

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