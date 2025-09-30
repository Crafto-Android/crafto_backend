package com.crafto.crafto_backend.request

import org.springframework.web.multipart.MultipartFile

data class CustomerIssueRequest(
    val customerId: String,
    val issueTitle: String,
    val issueContent: String,
    val categoryId: String,
    val governmentId: String,
    val governmentName: String,
    val districtId: String,
    val districtName: String,
    val locationDetails: String,
)