package com.crafto.crafto_backend.request

data class CustomerIssueRequest(
    val customerId: String,
    val issueTitle: String,
    val issueContent: String,
    val categoryId: String,
    val governmentId: String,
    val districtId: String,
    val locationDetails: String,
)