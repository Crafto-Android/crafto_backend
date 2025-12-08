package com.crafto.crafto_backend.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty

data class CraftsmanSetupRequest(
    @field:Valid
    val personalInfo: CraftsmanPersonalInfoDto,
    @field:NotEmpty(message = "At least one category must be selected")
    val categories: List<String>
)

data class CraftsmanUploadedFilesRequest(
    val idCardFrontUrl: String,
    val idCardBackUrl: String,
    val workImageUrls: List<String>
)



