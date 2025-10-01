package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.database.entity.Customer
import com.crafto.crafto_backend.request.CustomerRequest
import com.crafto.crafto_backend.response.CustomerResponse

fun Customer.toResponse() = CustomerResponse(
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