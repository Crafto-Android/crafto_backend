package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.entity.Customer
import com.crafto.crafto_backend.request.CustomerRequest
import com.crafto.crafto_backend.response.CustomerResponse
import org.bson.types.ObjectId

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