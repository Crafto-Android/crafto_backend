package com.crafto.crafto_backend.dto


data class AuthenticationRequest(
    val userId: String?,
    val isCustomer: Boolean,
)
