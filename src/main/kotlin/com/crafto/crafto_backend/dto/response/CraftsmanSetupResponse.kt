package com.crafto.crafto_backend.dto.response

import com.crafto.crafto_backend.database.entity.CraftsmanStatus
import com.crafto.crafto_backend.database.entity.PersonalInfo
import com.crafto.crafto_backend.database.entity.VerificationStatus
import com.crafto.crafto_backend.dto.request.CraftsmanUploadedFilesRequest
import java.time.Instant

data class CraftsmanSetupResponse(
    val craftsmanId: String,
    val status: CraftsmanStatus,
    val profilePictureUrl: String? = null,
    val message: String
)


data class CraftsmanProfileResponse(
    val craftsmanId: String,
    val personalInfo: PersonalInfo,
    val profilePictureUrl: String? = null,
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

