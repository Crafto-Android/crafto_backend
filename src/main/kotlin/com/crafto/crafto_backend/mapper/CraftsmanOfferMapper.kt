package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.database.entity.CraftsmanOffer
import com.crafto.crafto_backend.dto.CraftsmanOfferRequest
import com.crafto.crafto_backend.dto.CraftsmanOfferResponse
import java.time.Instant

fun CraftsmanOfferRequest.toEntity(isSelected: Boolean) = CraftsmanOffer(
    craftsmanId = craftsmanId,
    customerId = customerId,
    customerIssueId = customerIssueId,
    price = price,
    createdDate = Instant.now(),
    visitedDate = Instant.parse(visitedDate),
    message = message,
    isSelected = isSelected
)
        
fun CraftsmanOffer.toResponse() = CraftsmanOfferResponse(
    id = id ?: throw Exception("issue not fount"),
    craftsmanId = craftsmanId,
    customerId = customerId,
    customerIssueId = customerIssueId,
    price = price,
    createdDate = createdDate,
    visitedDate = visitedDate,
    message = message,
    isSelected = isSelected
)