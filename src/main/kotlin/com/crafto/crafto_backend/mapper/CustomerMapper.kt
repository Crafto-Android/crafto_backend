package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.database.entity.Customer
import com.crafto.crafto_backend.database.entity.CustomerLocation
import com.crafto.crafto_backend.database.entity.CustomerPersonalInfo
import com.crafto.crafto_backend.dto.request.CustomerRequest
import com.crafto.crafto_backend.dto.response.CustomerResponse

fun Customer.toResponse() = CustomerResponse(
    id = id?.toHexString() ?: throw Exception("issue not fount"),
    name = personalInfo.name,
    profilePhoto = profilePictureUrl,
    governorate = location.governorate,
    district = location.district,
    detailedLocation = location.detailedLocation,
    categories = categories
)

fun CustomerRequest.toEntity() = Customer(
    userId = "",
    personalInfo = CustomerPersonalInfo(name,""),
    location = CustomerLocation(
        governorate = governorate,
        district = district,
        detailedLocation = detailedLocation
    ),
    profilePictureUrl = profilePhoto,
    categories = categories,
)