package com.crafto.crafto_backend.response

data class CustomerResponse (
    val id: String,
    val name: String,
    val profilePhoto: String? = null,
    val governorate: String,
    val district: String,
    val detailedLocation: String,
    val categories: List<String>,
)