package com.crafto.crafto_backend.dto

import com.crafto.crafto_backend.database.entity.CustomerIssueStatus

data class CustomerIssueDetailsResponse (
    val id: String,
    val title: String,
    val description: String,
    val status: CustomerIssueStatus,
    val category: CategoryResponse,
    val customerId: String,
    val governmentId: String,
    val governmentName: String,
    val districtId: String,
    val districtName: String,
    val locationDetails: String,
    val photos: List<String>,
    val offers: List<CraftsmanOfferResponse>
)