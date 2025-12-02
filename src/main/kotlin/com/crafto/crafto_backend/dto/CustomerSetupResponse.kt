package com.crafto.crafto_backend.dto

import java.time.Instant

data class CustomerSetupResponse(
    val customerId: String,
    val message: String
)

data class CustomerProfileResponse(
    val customerId: String,
    val userId: String,
    val personalInfo: CustomerPersonalInfoDto,
    val profilePictureUrl: String? = null,
    val location: CustomerLocationDto,
    val categories: List<String>,
    val createdAt: Instant,
    val updatedAt: Instant
)