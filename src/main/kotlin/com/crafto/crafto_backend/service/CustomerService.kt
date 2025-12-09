package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.constant.AppConstants
import com.crafto.crafto_backend.database.entity.Customer
import com.crafto.crafto_backend.database.entity.CustomerIssueStatus
import com.crafto.crafto_backend.mapper.mapToCustomerIssueDetailsResponse
import com.crafto.crafto_backend.mapper.toCategoryResponse
import com.crafto.crafto_backend.mapper.toEntity
import com.crafto.crafto_backend.mapper.toResponse
import com.crafto.crafto_backend.database.repository.CategoryRepository
import com.crafto.crafto_backend.database.repository.CraftsmanOfferRepository
import com.crafto.crafto_backend.database.repository.CustomerIssueRepository
import com.crafto.crafto_backend.database.repository.CustomerRepository
import com.crafto.crafto_backend.dto.request.CustomerIssueRequest
import com.crafto.crafto_backend.dto.request.CustomerRequest
import com.crafto.crafto_backend.dto.response.CustomerResponse
import com.crafto.crafto_backend.dto.request.CustomerSetupRequest
import com.crafto.crafto_backend.exception.ConflictException
import com.crafto.crafto_backend.exception.ForbiddenException
import com.crafto.crafto_backend.exception.NotFoundException
import com.crafto.crafto_backend.dto.response.CustomerIssueDetailsResponse
import com.crafto.crafto_backend.dto.response.CustomerIssueResponse
import com.crafto.crafto_backend.utils.getFileExtension
import com.crafto.crafto_backend.utils.validateImageFile
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val customerIssueRepository: CustomerIssueRepository,
    private val categoryRepository: CategoryRepository,
    private val imageStorageService: ImageStorageService,
    private val offerRepository: CraftsmanOfferRepository,
    private val firebaseStorageService: FirebaseStorageService
) {
    fun saveCustomer(body: CustomerRequest): CustomerResponse {
        val customer = customerRepository.save(body.toEntity())
        return customer.toResponse()
    }

    @Transactional
    fun createCustomer(
        userId: String,
        setupRequest: CustomerSetupRequest
    ): Customer {

        // Check if customer already exists
        customerRepository.findByUserId(userId)?.let {
            throw ConflictException("Customer profile already exists for this user")
        }

        val customer = Customer(
            userId = userId,
            personalInfo = com.crafto.crafto_backend.database.entity.CustomerPersonalInfo(
                name = setupRequest.personalInfo.name,
                phoneNumber = setupRequest.personalInfo.phoneNumber
            ),
            profilePictureUrl = null,
            location = com.crafto.crafto_backend.database.entity.CustomerLocation(
                governorate = setupRequest.location.governorate,
                district = setupRequest.location.district,
                detailedLocation = setupRequest.location.detailedLocation
            ),
            categories = setupRequest.categories
        )

        return customerRepository.save(customer)
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
            .findById(ObjectId(body.customerId))
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

    @Transactional
    fun uploadProfilePicture(
        customerId: String,
        userId: String,
        profilePicture: MultipartFile
    ): Customer {

        val customer = validateCustomerOwnership(customerId, userId)

        // Validate file
        validateImageFile(profilePicture, "Profile picture")

        // Delete old photo if exists
        customer.profilePictureUrl?.let { oldUrl ->
            firebaseStorageService.deleteFileByUrlAsync(oldUrl)
        }

        // Upload new photo
        val profilePictureUrl = firebaseStorageService.uploadFile(
            file = profilePicture,
            folder = AppConstants.StoragePaths.customerProfilePicture(customerId),
            fileName = "profile-${System.currentTimeMillis()}.${getFileExtension(profilePicture)}"
        )

        // Update customer
        val updatedCustomer = customer.copy(
            profilePictureUrl = profilePictureUrl,
            updatedAt = Instant.now()
        )

        return customerRepository.save(updatedCustomer)
    }

    @Transactional
    fun deleteCustomerAccount(customerId: String, userId: String): Boolean {
        val customer = validateCustomerOwnership(customerId, userId)

        // Delete profile photo if exists
        customer.profilePictureUrl?.let { url ->
            firebaseStorageService.deleteFileByUrl(url)
        }

        // Delete customer record
        customerRepository.deleteById(ObjectId(customerId))

        return true
    }

    fun getCustomerByUserId(userId: String): Customer? {
        return customerRepository.findByUserId(userId)
    }

    fun getCustomerByDatabaseId(customerId: String): Customer {
        return customerRepository.findById(ObjectId(customerId))
            .orElseThrow { NotFoundException("Customer not found") }
    }

    private fun validateCustomerOwnership(customerId: String, userId: String): Customer {
        val customer = customerRepository.findById(ObjectId(customerId))
            .orElseThrow { NotFoundException("Customer not found") }

        if (customer.userId != userId) {
            throw ForbiddenException("You don't have permission to modify this profile")
        }

        return customer
    }

    companion object {
        private const val IMAGE_FOLDER_NAME = "customer_issues_images"
    }
}