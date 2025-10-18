package com.crafto.crafto_backend.dto

data class AuthorizationResponse(
    val userId: String,
    val isCustomer: Boolean,
)
