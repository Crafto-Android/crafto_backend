package com.crafto.crafto_backend.repository

import com.crafto.crafto_backend.entity.CraftsmanOffer
import org.springframework.data.mongodb.repository.MongoRepository

interface CraftsmanOfferRepository : MongoRepository<CraftsmanOffer, String>{
    fun findByCustomerId(customerId: String): List<CraftsmanOffer>
    fun findByCustomerIssueId(customerIssueId: String): List<CraftsmanOffer>
}