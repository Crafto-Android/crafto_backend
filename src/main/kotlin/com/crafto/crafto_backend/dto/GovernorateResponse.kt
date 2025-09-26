package com.crafto.crafto_backend.dto


data class GovernorateResponse(
    val id: String,
    val name: String,
    val districts: List<String>
)