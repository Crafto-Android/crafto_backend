package com.crafto.crafto_backend.dto.request

import jakarta.validation.constraints.Size

data class CustomerIssueRequest(
    val customerId: String,
    val issueTitle: String,
    @field:Size(min = 50, max = 200, message = "Description must between 50 to 200 char")
    val issueContent: String,
    val categoryId: String,
    val governmentId: String,
    val districtId: String,
    val locationDetails: String,
)