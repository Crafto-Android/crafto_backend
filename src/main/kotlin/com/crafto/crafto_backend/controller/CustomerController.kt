package com.crafto.crafto_backend.controller

import com.crafto.crafto_backend.dto.request.CustomerIssueRequest
import com.crafto.crafto_backend.dto.CustomerLocationDto
import com.crafto.crafto_backend.dto.CustomerPersonalInfoDto
import com.crafto.crafto_backend.dto.CustomerProfileResponse
import com.crafto.crafto_backend.dto.request.CustomerRequest
import com.crafto.crafto_backend.dto.request.CustomerSetupRequest
import com.crafto.crafto_backend.dto.CustomerSetupResponse
import com.crafto.crafto_backend.dto.response.ProfilePictureUploadResponse
import com.crafto.crafto_backend.dto.response.CustomerIssueResponse
import com.crafto.crafto_backend.service.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

// POST http://localhost:8085/customer
// POST http://localhost:8085/customer/issue
// GET  http://localhost:8085/customer/issue/customer_id/{customerId}
// GET  http://localhost:8085/customer/issue/details/customer_issue_id/{customerIssueId}
// GET  http://localhost:8085/customer/issue/details/customer_id/{customerId}
// POST http://localhost:8085/customer/issue/select_craftsman/{customerIssueId}

// POST http://localhost:8085/customer/setup
// POST http://localhost:8085/customer/{customerId}/profile-picture
// PATCH http://localhost:8085/customer/{customerId}
// GET http://localhost:8085/customer/profile
// GET http://localhost:8085/customer/{customerId}
// DELETE http://localhost:8085/customer/{customerId}/delete

@RestController
@RequestMapping("/customer")
class CustomerController(private val customerService: CustomerService) {

    @PostMapping
    fun saveCustomer(@RequestBody body: CustomerRequest) = customerService.saveCustomer(body = body)

    @PostMapping("/issue", consumes = ["multipart/form-data"])
    fun saveCustomerIssue(
        @RequestParam customerId: String,
        @RequestParam issueTitle: String,
        @RequestParam issueContent: String,
        @RequestParam categoryId: String,
        @RequestParam governmentId: String,
        @RequestParam districtId: String,
        @RequestParam locationDetails: String,
        @RequestParam photos: List<MultipartFile>
    ): CustomerIssueResponse {
        val body = CustomerIssueRequest(
            customerId = customerId,
            issueTitle = issueTitle,
            issueContent = issueContent,
            categoryId = categoryId,
            governmentId = governmentId,
            districtId = districtId,
            locationDetails = locationDetails
        )
        val response = customerService.saveCustomerIssue(body = body, photos)
        return response
    }

    @GetMapping("/issue/customer_id/{customerId}")
    fun getCustomerIssuesByCustomerId(@PathVariable customerId: String) =
        customerService.getCustomerIssues(customerId)

    @GetMapping("/issue/details/customer_issue_id/{customerIssueId}")
    fun getCustomerIssueDetailsById(@PathVariable customerIssueId: String) =
        customerService.getCustomerIssueDetails(customerIssueId)

    @GetMapping("/issue/details/customer_id/{customerId}")
    fun getCustomerIssueDetailsByCustomerId(@PathVariable customerId: String) =
        customerService.getCustomerIssuesDetailsByCustomerId(customerId)

    @PostMapping("/issue/select_craftsman/{customerIssueId}")
    fun selectCraftsman(@PathVariable customerIssueId: String, @RequestParam offerId: String,) =
        customerService.selectCraftsManOffer(issueId = customerIssueId, offerId = offerId)


    @PostMapping("/setup")
    fun setupCustomerProfile(
        @Valid @RequestBody setupRequest: CustomerSetupRequest,
        @RequestHeader("userId") userId: String
    ): ResponseEntity<CustomerSetupResponse> {

        val customer = customerService.createCustomer(userId, setupRequest)

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CustomerSetupResponse(
                customerId = customer.id!!.toHexString(),
                message = "Customer profile created successfully"
            )
        )
    }

    @PostMapping(
        "/{customerId}/profile-picture",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun uploadProfilePicture(
        @PathVariable customerId: String,
        @RequestParam("profilePicture") profilePicture: MultipartFile,
        @RequestHeader("userId") userId: String
    ): ResponseEntity<ProfilePictureUploadResponse> {

        val updatedCustomer = customerService.uploadProfilePicture(
            customerId = customerId,
            userId = userId,
            profilePicture = profilePicture
        )

        return ResponseEntity.ok(
            ProfilePictureUploadResponse(
                id = updatedCustomer.id!!.toHexString(),
                profilePictureUrl = updatedCustomer.profilePictureUrl!!,
                message = "Profile picture uploaded successfully"
            )
        )
    }

    @GetMapping("/profile")
    fun getCustomerProfile(
        @RequestHeader("userId") userId: String
    ): ResponseEntity<CustomerProfileResponse> {

        val customer = customerService.getCustomerByUserId(userId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(
            CustomerProfileResponse(
                customerId = customer.id!!.toHexString(),
                userId = customer.userId,
                personalInfo = CustomerPersonalInfoDto(
                    name = customer.personalInfo.name,
                    phoneNumber = customer.personalInfo.phoneNumber
                ),
                profilePictureUrl = customer.profilePictureUrl,
                location = CustomerLocationDto(
                    governorate = customer.location.governorate,
                    district = customer.location.district,
                    detailedLocation = customer.location.detailedLocation
                ),
                categories = customer.categories,
                createdAt = customer.createdAt,
                updatedAt = customer.updatedAt
            )
        )
    }

    @GetMapping("/{customerId}")
    fun getCustomerById(
        @PathVariable customerId: String
    ): ResponseEntity<CustomerProfileResponse> {

        val customer = customerService.getCustomerByDatabaseId(customerId)

        return ResponseEntity.ok(
            CustomerProfileResponse(
                customerId = customer.id!!.toHexString(),
                userId = customer.userId,
                personalInfo = CustomerPersonalInfoDto(
                    name = customer.personalInfo.name,
                    phoneNumber = customer.personalInfo.phoneNumber
                ),
                profilePictureUrl = customer.profilePictureUrl,
                location = CustomerLocationDto(
                    governorate = customer.location.governorate,
                    district = customer.location.district,
                    detailedLocation = customer.location.detailedLocation
                ),
                categories = customer.categories,
                createdAt = customer.createdAt,
                updatedAt = customer.updatedAt
            )
        )
    }

    @PostMapping("/{customerId}/delete")
    fun deleteCustomerAccount(
        @PathVariable customerId: String,
        @RequestHeader("userId") userId: String
    ): ResponseEntity<Map<String, String>> {

        val success = customerService.deleteCustomerAccount(customerId, userId)

        return if (success) {
            ResponseEntity.ok(mapOf("message" to "Customer account deleted successfully"))
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("message" to "Failed to delete customer account"))
        }
    }
}