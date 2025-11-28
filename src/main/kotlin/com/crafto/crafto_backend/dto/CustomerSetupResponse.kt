package com.crafto.crafto_backend.dto

import java.time.Instant

//data class CustomerSetupResponse (
//    val name: String,
//    val profilePhoto: String? = null,
//    val governorate: String,
//    val district: String,
//    val detailedLocation: String,
//    val categories: List<String>,
//)

data class CustomerSetupResponse(
    val customerId: String,
    val message: String
)

data class CustomerProfileResponse(
    val customerId: String,
    val userId: String,
    val personalInfo: CustomerPersonalInfoDto,
    val profilePhoto: String? = null,
    val location: CustomerLocationDto,
    val categories: List<String>,
    val createdAt: Instant,
    val updatedAt: Instant
)

data class CustomerProfilePictureUploadResponse(
    val customerId: String,
    val profilePhotoUrl: String,
    val message: String
)