package com.crafto.crafto_backend.dto

import com.crafto.crafto_backend.database.entity.CustomerIssueStatus
import java.time.Instant

data class CustomerIssueCreateResponse(
    val issueId: String,
    val message: String
)

/**
 * issue response (for lists)
 */
data class CustomerIssueResponse(
    val issueId: String,
    val customerId: String,
    val title: String,
    val description: String,
    val status: CustomerIssueStatus,
    val categoryId: String,
    val categoryName: String?=null,
    val location: IssueLocationResponseDto,
    val photos: List<String>?,
    val photosCount: Int,
    val offersCount: Int,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class CustomerIssueDetailsResponse (
    val issueId: String,
    val customerId: String,
    val title: String,
    val description: String,
    val status: CustomerIssueStatus,
    val category: CategoryResponse,

    val location: IssueLocationResponseDto,
    val photos: List<String>,
    val offers: List<CraftsmanOfferResponse>,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class IssueLocationResponseDto(
    val governorateId: String,
    val governorateName: String,
    val districtId: String,
    val districtName: String,
    val detailedLocation: String
)

data class CustomerIssuesListResponse(
    val issues: List<CustomerIssueResponse>,
    val totalCount: Int
)

data class IssuePhotosUploadResponse(
    val issueId: String,
    val photoUrls: List<String>,
    val totalPhotos: Int,
    val message: String
)

data class SelectOfferResponse(
    val issueId: String,
    val selectedOfferId: String,
    val newStatus: CustomerIssueStatus,
    val message: String
)