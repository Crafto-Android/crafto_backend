package com.crafto.crafto_backend.dto.request

import com.crafto.crafto_backend.dto.CustomerLocationDto
import com.crafto.crafto_backend.dto.CustomerPersonalInfoDto
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class CustomerSetupRequest(
    @field:Valid
    val personalInfo: CustomerPersonalInfoDto,

    @field:Valid
    val location: CustomerLocationDto,

    @field:NotEmpty(message = "At least one category must be selected")
    @field:Size(min = 1, max = 5, message = "Categories must be between 1 and 5")
    val categories: List<String>
)

