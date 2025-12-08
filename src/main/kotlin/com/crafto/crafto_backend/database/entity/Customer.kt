package com.crafto.crafto_backend.database.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("customer")
data class Customer (
    @Id
    val id: ObjectId? = null,
    val userId: String,
    val personalInfo: CustomerPersonalInfo,
    val profilePictureUrl: String? = null,
    val location: CustomerLocation,
    val categories: List<String>,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
)

data class CustomerPersonalInfo(
    val name: String,
    val phoneNumber: String? = null
)

data class CustomerLocation(
    val governorate: String,
    val district: String,
    val detailedLocation: String
)