package com.crafto.crafto_backend.dto.response

data class AuthorizationResponse(
    val userId: String,
    val isCustomer: Boolean,
)
