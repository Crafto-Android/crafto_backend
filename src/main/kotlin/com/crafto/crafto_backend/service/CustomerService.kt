package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.constant.AppConstants
import com.crafto.crafto_backend.database.entity.Customer
import com.crafto.crafto_backend.database.repository.CustomerRepository
import com.crafto.crafto_backend.dto.CustomerSetupRequest
import com.crafto.crafto_backend.exception.ConflictException
import com.crafto.crafto_backend.exception.ForbiddenException
import com.crafto.crafto_backend.exception.NotFoundException
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
    private val firebaseStorageService: FirebaseStorageService
) {
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
}