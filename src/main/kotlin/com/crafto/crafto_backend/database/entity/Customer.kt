package com.crafto.crafto_backend.database.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("customer")
data class Customer (
    @Id
    val id: String? = null,
    val name: String,
    val profilePhoto: String? = null,
    val governorate: String,
    val district: String,
    val detailedLocation: String,
    val categories: List<String>,
)