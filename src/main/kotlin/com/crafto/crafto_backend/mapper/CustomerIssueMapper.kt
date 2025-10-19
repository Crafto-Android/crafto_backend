package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.entity.Category
import com.crafto.crafto_backend.entity.CustomerIssue
import com.crafto.crafto_backend.entity.CustomerIssueStatus
import com.crafto.crafto_backend.request.CustomerIssueRequest
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