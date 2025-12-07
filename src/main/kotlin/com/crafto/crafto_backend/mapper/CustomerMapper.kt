package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.database.entity.Customer
import com.crafto.crafto_backend.dto.CustomerRequest
import com.crafto.crafto_backend.dto.CustomerResponse

fun Customer.toResponse() = CustomerResponse(
    id = id ?: throw Exception("issue not fount"),
    name = name,
    profilePhoto = profilePhoto,
    governorate = governorate,
    district = district,
    detailedLocation = detailedLocation,
    categories = categories
)

fun CustomerRequest.toEntity() = Customer(
    name = name,
    profilePhoto = profilePhoto,
    governorate = governorate,
    district = district,
    detailedLocation = detailedLocation,
    categories = categories
)