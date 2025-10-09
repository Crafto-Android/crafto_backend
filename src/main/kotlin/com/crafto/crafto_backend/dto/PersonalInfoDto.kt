package com.crafto.crafto_backend.dto

import com.crafto.crafto_backend.constant.AppConstants.Validation.PHONE_REGEX
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class PersonalInfoDto(
    @field:NotBlank(message = "First name is required")
    val firstName: String,
    @field:NotBlank(message = "Last name is required")
    val lastName: String,
    @field:Pattern(regexp = PHONE_REGEX, message = "Invalid phone number")
    val phoneNumber: String,
    val address: String?
)