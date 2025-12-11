package com.crafto.crafto_backend.dto.request


data class AuthorizationRequest(
    val userId: String?,
    val isCustomer: Boolean,
)
