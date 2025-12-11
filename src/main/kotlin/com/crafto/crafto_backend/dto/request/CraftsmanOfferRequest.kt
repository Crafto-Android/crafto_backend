package com.crafto.crafto_backend.dto.request

data class CraftsmanOfferRequest(
    val craftsmanId: String,
    val customerId: String,
    val customerIssueId: String,
    val price: Double,
    val visitedDate: String,
    val message: String,
)