package com.crafto.crafto_backend.dto.request

data class CustomerRequest(
    val name: String,
    val profilePhoto: String? = null,
    val governorate: String,
    val district: String,
    val detailedLocation: String,
    val categories: List<String>,
)