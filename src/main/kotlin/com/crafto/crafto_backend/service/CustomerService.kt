package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.entity.CustomerIssueStatus
import com.crafto.crafto_backend.mapper.toEntity
import com.crafto.crafto_backend.mapper.toResponse
import com.crafto.crafto_backend.repository.CategoryRepository
import com.crafto.crafto_backend.repository.CustomerIssueRepository
import com.crafto.crafto_backend.repository.CustomerRepository
import com.crafto.crafto_backend.request.CustomerIssueRequest
import com.crafto.crafto_backend.request.CustomerRequest
import com.crafto.crafto_backend.response.CustomerIssueResponse
import com.crafto.crafto_backend.response.CustomerResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val customerIssueRepository: CustomerIssueRepository,
    private val categoryRepository: CategoryRepository,
    private val imageStorageService: ImageStorageService,
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
        val urls = uploadProductImages(body.customerId, photos)
        val issue = customerIssueRepository.save(
            body.toEntity(
                imgs = urls,
                status = CustomerIssueStatus.SUBMITTED
            )
        )
        return issue.toResponse()
    }

    @Transactional
    fun uploadProductImages(customerId: String, files: List<MultipartFile>): List<String> {
        val customer = customerRepository.findById(customerId)
            .orElseThrow {
                Exception()
            }
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

    companion object {
        private const val IMAGE_FOLDER_NAME = "customer_issues_images"
    }
}