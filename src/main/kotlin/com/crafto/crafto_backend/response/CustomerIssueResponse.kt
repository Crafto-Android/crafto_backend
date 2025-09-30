package com.crafto.crafto_backend.response

import com.crafto.crafto_backend.entity.Category

data class CustomerIssueResponse(
    val customerId: String,
    val issueTitle: String,
    val issueContent: String,
    val category: Category,
    val governmentName: String,
    val districtName: String,
    val locationDetails: String,
    val photos: List<String>
)