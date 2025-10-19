package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.dto.CategoryResponse
import com.crafto.crafto_backend.entity.CustomerIssue
import com.crafto.crafto_backend.entity.CustomerIssueStatus
import com.crafto.crafto_backend.request.CustomerIssueRequest
import com.crafto.crafto_backend.response.CraftsmanOfferResponse
import com.crafto.crafto_backend.response.CustomerIssueDetailsResponse
import com.crafto.crafto_backend.response.CustomerIssueResponse

fun CustomerIssueRequest.toEntity(imgs: List<String>, status: CustomerIssueStatus) = CustomerIssue(
    customerId = customerId,
    title = issueTitle,
    description = issueContent,
    governmentId = governmentId,
    districtId = districtId,
    photos = imgs,
    locationDetails = locationDetails,
    categoryId = categoryId,
    status = status
)

fun CustomerIssue.toResponse() = CustomerIssueResponse(
    id = id ?: throw Exception("issue not fount"),
    title = title,
    description = description,
    status = status,
    customerId = customerId,
    categoryId = categoryId,
    governmentId = governmentId,
    districtId = districtId,
    locationDetails = locationDetails,
    photos = photos
)

fun mapToCustomerIssueDetailsResponse(
    issue: CustomerIssueResponse,
    category: CategoryResponse,
    offers: List<CraftsmanOfferResponse>
) =
    CustomerIssueDetailsResponse(
        id = issue.id,
        title = issue.title,
        description = issue.description,
        status = issue.status,
        category = category,
        customerId = issue.customerId,
        governmentId = issue.governmentId,
        governmentName = "",
        districtId = issue.districtId,
        districtName = "",
        locationDetails = issue.locationDetails,
        photos = issue.photos,
        offers = offers
    )