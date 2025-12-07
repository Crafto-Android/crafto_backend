package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.entity.Customer
import com.crafto.crafto_backend.entity.CustomerIssueStatus
import com.crafto.crafto_backend.mapper.mapToCustomerIssueDetailsResponse
import com.crafto.crafto_backend.mapper.toCategoryResponse
import com.crafto.crafto_backend.mapper.toEntity
import com.crafto.crafto_backend.mapper.toResponse
import com.crafto.crafto_backend.repository.CategoryRepository
import com.crafto.crafto_backend.repository.CraftsmanOfferRepository
import com.crafto.crafto_backend.repository.CustomerIssueRepository
import com.crafto.crafto_backend.repository.CustomerRepository
import com.crafto.crafto_backend.dto.CustomerIssueRequest
import com.crafto.crafto_backend.dto.CustomerRequest
import com.crafto.crafto_backend.dto.CustomerResponse
import com.crafto.crafto_backend.response.CustomerIssueDetailsResponse
import com.crafto.crafto_backend.response.CustomerIssueResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val customerIssueRepository: CustomerIssueRepository,
    private val categoryRepository: CategoryRepository,
    private val imageStorageService: ImageStorageService,
    private val offerRepository: CraftsmanOfferRepository,
) {
    fun saveCustomer(body: CustomerRequest): CustomerResponse {
        val customer = customerRepository.save(body.toEntity())
        return customer.toResponse()
    }

    fun getCustomerIssues(customerId: String): List<CustomerIssueResponse> {
        val issues = customerIssueRepository.findByCustomerId(customerId)
        return issues.map { it.toResponse() }
    }

    fun saveCustomerIssue(
        body: CustomerIssueRequest,
        photos: List<MultipartFile>
    ): CustomerIssueResponse {
        val customer = customerRepository
            .findById(body.customerId)
            .orElseThrow { IllegalArgumentException("customer not found") }

        val category = categoryRepository
            .findById(body.categoryId)
            .orElseThrow { IllegalArgumentException("category not found") }

        val urls = uploadProductImages(customer, photos)

        val issue = customerIssueRepository.save(
            body.toEntity(
                imgs = urls,
                status = CustomerIssueStatus.SUBMITTED
            )
        )

        val updatedCategory = category.copy(popularity = category.popularity + 1)
        categoryRepository.save(updatedCategory)

        return issue.toResponse()
    }

    @Transactional
    fun uploadProductImages(customer: Customer, files: List<MultipartFile>): List<String> {
        val imageUrls = mutableListOf<String>()
        files.forEach { file ->
            val imageUrl = imageStorageService.uploadImage(
                file = file,
                fileName = "${customer.id}-${file.originalFilename}",
                folderName = IMAGE_FOLDER_NAME
            )
            imageUrls.add(imageUrl)
        }
        return imageUrls
    }

    fun getCustomerIssueDetails(customerIssueId: String): CustomerIssueDetailsResponse {
        val issue = customerIssueRepository
            .findById(customerIssueId)
            .orElseThrow { IllegalArgumentException("customer issue not found") }
            .toResponse()

        return getCustomerIssueDetails(issue)
    }

    fun getCustomerIssuesDetailsByCustomerId(customerId: String): List<CustomerIssueDetailsResponse> {
        return customerIssueRepository
            .findByCustomerId(customerId)
            .map { getCustomerIssueDetails(it.toResponse()) }
    }

    fun selectCraftsManOffer(issueId: String, offerId: String){
        val customerIssue = customerIssueRepository
            .findById(issueId)
            .orElseThrow { IllegalArgumentException("issue not found") }

        val offer = offerRepository
            .findById(offerId)
            .orElseThrow { IllegalArgumentException("offer not found") }

        val updatedIssue = customerIssue.copy(status = CustomerIssueStatus.CRAFTSMAN_SELECTED)
        customerIssueRepository.save(updatedIssue)

        val updatedOffer = offer.copy(isSelected = true)
        offerRepository.save(updatedOffer)

    }

    private fun getCustomerIssueDetails(issue: CustomerIssueResponse): CustomerIssueDetailsResponse {
        val offers = offerRepository
            .findByCustomerIssueId(customerIssueId = issue.id)
            .map { it.toResponse() }

        val category = categoryRepository
            .findById(issue.categoryId)
            .orElseThrow { IllegalArgumentException("category not found") }
            .toCategoryResponse()

        return mapToCustomerIssueDetailsResponse(
            issue = issue,
            category = category,
            offers = offers
        )
    }

    companion object {
        private const val IMAGE_FOLDER_NAME = "customer_issues_images"
    }
}