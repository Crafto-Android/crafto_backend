package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.entity.CraftsmanOffer
import com.crafto.crafto_backend.request.CraftsmanOfferRequest
import com.crafto.crafto_backend.response.CraftsmanOfferResponse
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