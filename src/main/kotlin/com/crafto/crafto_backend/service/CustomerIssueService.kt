package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.database.entity.CustomerIssue
import com.crafto.crafto_backend.database.entity.CustomerIssueStatus
import com.crafto.crafto_backend.database.entity.IssueLocation
import com.crafto.crafto_backend.database.repository.CategoryRepository
import com.crafto.crafto_backend.database.repository.CraftsmanOfferRepository
import com.crafto.crafto_backend.database.repository.CustomerIssueRepository
import com.crafto.crafto_backend.database.repository.CustomerRepository
import com.crafto.crafto_backend.dto.CustomerIssueDetailsResponse
import com.crafto.crafto_backend.dto.CustomerIssueRequest
import com.crafto.crafto_backend.dto.CustomerIssueResponse
import com.crafto.crafto_backend.exception.NotFoundException
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomerIssueService(
    private val customerIssueRepository: CustomerIssueRepository,
    private val customerRepository: CustomerRepository,
    private val craftsmanOfferRepository: CraftsmanOfferRepository,
    private val categoryRepository: CategoryRepository,
    private val firebaseStorageService: FirebaseStorageService
) {
    @Transactional
    fun createIssue(
        userId: String,
        request: CustomerIssueRequest
    ): CustomerIssue {

        // Verify customer exists
        val customer = customerRepository.findByUserId(userId)
            ?: throw NotFoundException("Customer profile not found. Please create a profile first.")

        // Verify category exists
        if (!categoryRepository.existsById(ObjectId(request.categoryId))) {
            throw NotFoundException("Category not found")
        }

        val issue = CustomerIssue(
            customerId = userId,  // Use auth userId, NOT from request body
            title = request.title,
            description = request.description,
            status = CustomerIssueStatus.SUBMITTED,
            categoryId = ObjectId(request.categoryId),
            location = IssueLocation(
                governorateId = request.location.governorateId,
                governorateName = request.location.governorateName,
                districtId = request.location.districtId,
                districtName = request.location.districtName,
                detailedLocation = request.location.detailedLocation
            ),
            photos = emptyList()
        )

        return customerIssueRepository.save(issue)
    }

    fun getCustomerIssues(userId: String): List<CustomerIssueResponse> {
        val issues = customerIssueRepository.findByCustomerId(userId)

        return issues.map { issue ->
            mapToIssueResponse(issue)
        }
    }


    fun getIssueDetails(issueId: String): CustomerIssueDetailsResponse {
        val issue = customerIssueRepository.findById(issueId)
            .orElseThrow { NotFoundException("Issue not found") }

        return mapToIssueDetailResponse(issue)
    }

    fun getIssueDetailsForCustomer(
        issueId: String,
        userId: String
    ): CustomerIssueDetailsResponse {
        val issue = validateIssueOwnership(issueId, userId)
        return mapToIssueDetailResponse(issue)
    }






}



