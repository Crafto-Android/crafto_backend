package com.crafto.crafto_backend.dto


data class AuthorizationRequest(
    val userId: String?,
    val isCustomer: Boolean,
)
