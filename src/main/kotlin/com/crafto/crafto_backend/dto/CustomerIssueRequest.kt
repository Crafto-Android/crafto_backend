package com.crafto.crafto_backend.dto

import com.crafto.crafto_backend.database.entity.CustomerIssueStatus
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CustomerIssueRequest(
    /**
     * Note: customerId comes from userId header (not request body)
     */

    @field:NotBlank(message = "Title is required")
    @field:Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    val title: String,

    @field:NotBlank(message = "Description is required")
    @field:Size(min = 20, max = 1000, message = "Description must be between 20 and 1000 characters")
    val description: String,

    val customerId: String,
    val issueTitle: String,
    @field:NotBlank(message = "Category ID is required")
    val categoryId: String,

    @field:Valid
    @field:NotNull(message = "Location is required")
    val location: IssueLocationDto
)

data class IssueLocationDto(
    @field:NotBlank(message = "Governorate ID is required")
    val governorateId: String,

    @field:NotBlank(message = "Governorate name is required")
    val governorateName: String,

    @field:NotBlank(message = "District ID is required")
    val districtId: String,

    @field:NotBlank(message = "District name is required")
    val districtName: String,

    @field:NotBlank(message = "Detailed location is required")
    val detailedLocation: String
)

data class CustomerIssueStatusUpdateRequest(
    @field:NotNull(message = "Status is required")
    val status: CustomerIssueStatus
)

data class SelectOfferRequest(
    @field:NotBlank(message = "Offer ID is required")
    val offerId: String
)