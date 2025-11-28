package com.crafto.crafto_backend.dto

import com.crafto.crafto_backend.constant.AppConstants.Validation.PHONE_REGEX
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class CustomerPersonalInfoDto(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String,

    @field:Pattern(regexp = PHONE_REGEX, message = "Invalid phone number")
    val phoneNumber: String? = null
)