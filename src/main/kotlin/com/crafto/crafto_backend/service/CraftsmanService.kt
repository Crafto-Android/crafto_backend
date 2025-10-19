package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.entity.CustomerIssueStatus
import com.crafto.crafto_backend.mapper.toEntity
import com.crafto.crafto_backend.mapper.toResponse
import com.crafto.crafto_backend.repository.CraftsmanOfferRepository
import com.crafto.crafto_backend.repository.CustomerIssueRepository
import com.crafto.crafto_backend.request.CraftsmanOfferRequest
import com.crafto.crafto_backend.response.CraftsmanOfferResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CraftsmanService(
    private val craftsManOfferRepository: CraftsmanOfferRepository,
    private val customerIssueRepository: CustomerIssueRepository,
){
    @Transactional
    fun saveCraftsmanOffer(body: CraftsmanOfferRequest): CraftsmanOfferResponse{

        val customerIssue = customerIssueRepository.findById(body.customerIssueId)
            .orElseThrow { IllegalArgumentException("Customer issue not found") }

        val updatedIssue = customerIssue.copy(status = CustomerIssueStatus.RECEIVING_OFFERS)
        customerIssueRepository.save(updatedIssue)

        val offer = craftsManOfferRepository.save(body.toEntity(isSelected = false))
        return offer.toResponse()
    }

    fun getOffersByCustomerId(customerId: String): List<CraftsmanOfferResponse> {
        val issues = craftsManOfferRepository.findByCustomerId(customerId)
        return issues.map { it.toResponse() }
    }

    fun getOffersByCustomerIssueId(customerIssueId: String): List<CraftsmanOfferResponse> {
        val issues = craftsManOfferRepository.findByCustomerIssueId(customerIssueId)
        return issues.map { it.toResponse() }
    }
}