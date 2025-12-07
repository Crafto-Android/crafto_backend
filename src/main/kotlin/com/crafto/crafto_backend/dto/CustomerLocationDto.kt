package com.crafto.crafto_backend.dto

import jakarta.validation.constraints.NotBlank

data class CustomerLocationDto(
    @field:NotBlank(message = "Governorate is required")
    val governorate: String,

    @field:NotBlank(message = "District is required")
    val district: String,

    @field:NotBlank(message = "Detailed location is required")
    val detailedLocation: String
)