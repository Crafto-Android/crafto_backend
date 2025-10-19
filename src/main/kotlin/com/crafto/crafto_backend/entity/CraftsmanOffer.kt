package com.crafto.crafto_backend.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("craftsman_offer")
data class CraftsmanOffer(
    @Id
    val id: String? = null,
    val craftsmanId: String,
    val customerId: String,
    val customerIssueId: String,
    val price: Double,
    val createdDate: Instant,
    val visitedDate: Instant,
    val message: String,
    val isSelected: Boolean
)