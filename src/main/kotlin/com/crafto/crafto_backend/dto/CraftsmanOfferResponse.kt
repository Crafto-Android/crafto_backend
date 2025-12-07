package com.crafto.crafto_backend.dto

import java.time.Instant

data class CraftsmanOfferResponse(
    val id: String,
    val craftsmanId: String,
    val customerId: String,
    val customerIssueId: String,
    val price: Double,
    val createdDate: Instant,
    val visitedDate: Instant,
    val message: String,
    val isSelected: Boolean
)
