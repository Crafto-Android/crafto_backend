package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.entity.Category
import com.crafto.crafto_backend.entity.CustomerIssue
import com.crafto.crafto_backend.request.CustomerIssueRequest
import com.crafto.crafto_backend.response.CustomerIssueResponse

fun CustomerIssueRequest.toEntity(imgs: List<String>) = CustomerIssue(
    customerId = customerId,
    issueTitle = issueTitle,
    issueContent = issueContent,
    governmentId = governmentId,
    governmentName = governmentName,
    districtId = districtId,
    districtName = districtName,
    photos = imgs,
    locationDetails = locationDetails,
    categoryId = categoryId,
)

fun CustomerIssue.toResponse(category: Category) = CustomerIssueResponse(
    customerId = customerId,
    issueTitle = issueTitle,
    issueContent = issueContent,
    governmentName = governmentName,
    districtName = districtName,
    photos = photos,
    category = category,
    locationDetails = locationDetails,
)