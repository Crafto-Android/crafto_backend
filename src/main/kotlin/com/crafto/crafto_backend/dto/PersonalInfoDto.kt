package com.crafto.crafto_backend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class PersonalInfoDto(
    @field:NotBlank(message = "First name is required")
    val firstName: String,
    @field:NotBlank(message = "Last name is required")
    val lastName: String,
    @field:Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    val phoneNumber: String,
    val address: String?
)