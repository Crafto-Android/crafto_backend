package com.crafto.crafto_backend.controller

import com.crafto.crafto_backend.constant.ApiEndpoints
import com.crafto.crafto_backend.constant.AppConstants.Validation.MAX_WORK_IMAGES
import com.crafto.crafto_backend.constant.AppConstants.Validation.MIN_WORK_IMAGES
import com.crafto.crafto_backend.dto.CraftsmanProfileResponse
import com.crafto.crafto_backend.dto.CraftsmanSetupRequest
import com.crafto.crafto_backend.dto.CraftsmanSetupResponse
import com.crafto.crafto_backend.dto.CraftsmanStatusResponse
import com.crafto.crafto_backend.dto.IdCardUploadResponse
import com.crafto.crafto_backend.dto.VerificationInfo
import com.crafto.crafto_backend.dto.WorkPortfolioUploadResponse
import com.crafto.crafto_backend.service.CraftsmanService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

// POST  http://localhost:8085/craftsman/setup
// POST  http://localhost:8085/craftsman/{craftsmanId}/verify/id-cards
// POST  http://localhost:8085/craftsman/{craftsmanId}/verify/work-portfolio
// POST  http://localhost:8085/craftsman/{craftsmanId}/delete-account
// GET   http://localhost:8085/craftsman/{craftsmanId}/status
// GET   http://localhost:8085/craftsman/profile
// GET   http://localhost:8085/craftsman/{craftsmanId}

@RestController
@RequestMapping(ApiEndpoints.Craftsman.BASE)
class CraftsmanController(
    private val craftsmanService: CraftsmanService
) {

    @PostMapping(ApiEndpoints.Craftsman.SETUP)
    fun setupBasicInfo(
        @Valid @RequestBody setupRequest: CraftsmanSetupRequest,
        @RequestHeader("userId") userId: String
    ): ResponseEntity<CraftsmanSetupResponse> {

        val craftsman = craftsmanService.createCraftsman(userId, setupRequest)

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CraftsmanSetupResponse(
                craftsmanId = craftsman.id!!.toHexString(),
                status = craftsman.status,
                message = "Profile created successfully. Please upload verification documents."
            )
        )
    }

    @PostMapping(ApiEndpoints.Craftsman.Verify.ID_CARDS, consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadIdCards(
        @PathVariable craftsmanId: String,
        @RequestParam("idCardFront") idCardFront: MultipartFile,
        @RequestParam("idCardBack") idCardBack: MultipartFile,
        @RequestHeader("userId") userId: String
    ): ResponseEntity<IdCardUploadResponse> {

        val updatedCraftsman = craftsmanService.uploadIdCards(
            craftsmanId = craftsmanId,
            userId = userId,
            idCardFront = idCardFront,
            idCardBack = idCardBack
        )

        return ResponseEntity.ok(
            IdCardUploadResponse(
                craftsmanId = updatedCraftsman.id!!.toHexString(),
                idCardFrontUrl = updatedCraftsman.verification.idCardFront!!,
                idCardBackUrl = updatedCraftsman.verification.idCardBack!!,
                message = "ID cards uploaded successfully",
                idVerificationStatus = "PENDING_VERIFICATION" // Ready for future AI validation
            )
        )
    }

    // Upload work verification images only
    @PostMapping(ApiEndpoints.Craftsman.Verify.WORK_PORTFOLIO, consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadWorkPortfolio(
        @PathVariable craftsmanId: String,
        @RequestParam("workImages") workImages: List<MultipartFile>,
        @RequestHeader("userId") userId: String
    ): ResponseEntity<WorkPortfolioUploadResponse> {

        // Validate work images count
        if (workImages.isEmpty() || workImages.size > MAX_WORK_IMAGES) {
            return ResponseEntity.badRequest().body(
                WorkPortfolioUploadResponse(
                    craftsmanId = craftsmanId,
                    workImageUrls = emptyList(),
                    message = "Please upload between $MIN_WORK_IMAGES and $MAX_WORK_IMAGES work images",
                    totalImages = 0
                )
            )
        }

        val updatedCraftsman = craftsmanService.uploadWorkPortfolio(
            craftsmanId = craftsmanId,
            userId = userId,
            workImages = workImages
        )

        return ResponseEntity.ok(
            WorkPortfolioUploadResponse(
                craftsmanId = updatedCraftsman.id!!.toHexString(),
                workImageUrls = updatedCraftsman.verification.workVerificationImages,
                message = "Work portfolio uploaded successfully",
                totalImages = updatedCraftsman.verification.workVerificationImages.size
            )
        )
    }

    @GetMapping(ApiEndpoints.Craftsman.STATUS)
    fun getCraftsmanStatus(
        @PathVariable craftsmanId: String
    ): ResponseEntity<CraftsmanStatusResponse> {
        return ResponseEntity.ok(craftsmanService.getCraftsmanStatus(craftsmanId))
    }

    // Get craftsman profile
    @GetMapping(ApiEndpoints.Craftsman.PROFILE)
    fun getCraftsmanProfile(
        @RequestHeader("userId") userId: String
    ): ResponseEntity<CraftsmanProfileResponse> {
        val craftsman = craftsmanService.getCraftsmanByCraftsmanId(userId)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(
            CraftsmanProfileResponse(
                craftsmanId = craftsman.id!!.toHexString(),
                personalInfo = craftsman.personalInfo,
                categories = craftsman.categories,
                status = craftsman.status,
                verificationInfo = VerificationInfo(
                    status = craftsman.verification.verificationStatus,
                    idCardFrontUrl = craftsman.verification.idCardFront,
                    idCardBackUrl = craftsman.verification.idCardBack,
                    workPortfolioUrls = craftsman.verification.workVerificationImages
                ),
                createdAt = craftsman.createdAt
            )
        )
    }

    @GetMapping(ApiEndpoints.Craftsman.BY_ID)
    fun getCraftsmanByDatabaseId(
        @PathVariable craftsmanId: String
    ): ResponseEntity<CraftsmanProfileResponse> {
        val craftsman = craftsmanService.getCraftsmanByDatabaseId(craftsmanId)

        // Only return public information
        return ResponseEntity.ok(
            CraftsmanProfileResponse(
                craftsmanId = craftsman.id!!.toHexString(),
                personalInfo = craftsman.personalInfo,
                categories = craftsman.categories,
                status = craftsman.status,
                verificationInfo = VerificationInfo(
                    status = craftsman.verification.verificationStatus,
                    idCardFrontUrl = craftsman.verification.idCardFront,
                    idCardBackUrl = craftsman.verification.idCardBack,
                    workPortfolioUrls = craftsman.verification.workVerificationImages
                ),
                createdAt = craftsman.createdAt
            )
        )
    }

    @PostMapping(ApiEndpoints.Craftsman.DELETE_ACCOUNT)
    fun deleteCraftsmanAccount(
        @PathVariable craftsmanId: String,
        @RequestHeader("userId") userId: String
    ): ResponseEntity<Map<String, String>> {
        val success = craftsmanService.deleteCraftsmanAccount(craftsmanId, userId)
        return if (success) {
            ResponseEntity.ok(mapOf("message" to "Craftsman account deleted successfully"))
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("message" to "Failed to delete craftsman account"))
        }
    }
}