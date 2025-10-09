package com.crafto.crafto_backend.dto

import com.crafto.crafto_backend.database.entity.CraftsmanStatus
import com.crafto.crafto_backend.database.entity.PersonalInfo
import com.crafto.crafto_backend.database.entity.VerificationStatus
import java.time.Instant

data class CraftsmanSetupResponse(
    val craftsmanId: String,
    val status: CraftsmanStatus,
    val message: String
)


data class CraftsmanProfileResponse(
    val craftsmanId: String,
    val personalInfo: PersonalInfo,
    val categories: List<String>,
    val status: CraftsmanStatus,
    val verificationInfo: VerificationInfo,
    val createdAt: Instant,
)

data class VerificationInfo(
    val status: VerificationStatus,
    val idCardFrontUrl: String? = null,
    val idCardBackUrl: String? = null,
    val workPortfolioUrls: List<String> = emptyList()
)

data class VerificationUploadResponse(
    val craftsmanId: String,
    val verificationStatus: VerificationStatus?,
    val message: String,
    val craftsmanUploadedFilesRequest: CraftsmanUploadedFilesRequest?
)

data class CraftsmanStatusResponse(
    val craftsmanId: String,
    val status: CraftsmanStatus,
    val verificationStatus: VerificationStatus,
    val message: String
)

data class IdCardUploadResponse(
    val craftsmanId: String,
    val idCardFrontUrl: String,
    val idCardBackUrl: String,
    val message: String,
    val idVerificationStatus: String // For future AI validation
)

data class WorkPortfolioUploadResponse(
    val craftsmanId: String,
    val workImageUrls: List<String>,
    val message: String,
    val totalImages: Int
)
