package com.crafto.crafto_backend.dto

data class CustomerResponse (
    val name: String,
    val profilePhoto: String? = null,
    val governorate: String,
    val district: String,
    val detailedLocation: String,
    val categories: List<String>,
)