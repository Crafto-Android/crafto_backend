package com.crafto.crafto_backend.dto

data class AuthenticationResponse(
    val userId: String,
    val isCustomer: Boolean,
)
