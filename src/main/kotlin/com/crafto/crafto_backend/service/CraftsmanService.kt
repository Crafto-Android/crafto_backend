package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.database.entity.*
import com.crafto.crafto_backend.database.repository.CraftsmanRepository
import com.crafto.crafto_backend.dto.*
import com.crafto.crafto_backend.exception.*
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

@Service
class CraftsmanService(
    private val craftsmanRepository: CraftsmanRepository,
    private val firebaseStorageService: FirebaseStorageService
) {

    fun createCraftsman(
        userId: String,
        setupRequest: CraftsmanSetupRequest
    ): Craftsman {
        // Check if craftsman already exists
        craftsmanRepository.findByUserId(userId)?.let {
            throw ConflictException("Craftsman profile already exists for this user")
        }

        val craftsman = Craftsman(
            userId = userId,
            personalInfo = PersonalInfo(
                firstName = setupRequest.personalInfo.firstName,
                lastName = setupRequest.personalInfo.lastName,
                phoneNumber = setupRequest.personalInfo.phoneNumber,
                address = setupRequest.personalInfo.address
            ),
            verification = Verification(
                idCardFront = null,
                idCardBack = null,
                workVerificationImages = emptyList(),
                verificationStatus = VerificationStatus.NOT_SUBMITTED
            ),
            categories = setupRequest.categories
        )

        return craftsmanRepository.save(craftsman)
    }

    @Transactional
    fun uploadIdCards(
        craftsmanId: String,
        userId: String,
        idCardFront: MultipartFile,
        idCardBack: MultipartFile
    ): Craftsman {
        val craftsman = validateCraftsmanOwnership(craftsmanId, userId)
        validateIdCardFiles(listOf(idCardFront, idCardBack))

        // Store old URLs in case we need to restore
        val oldIdFront = craftsman.verification.idCardFront
        val oldIdBack = craftsman.verification.idCardBack

        var newIdFrontUrl: String? = null
        var newIdBackUrl: String? = null

        try {
            // Upload new files
            newIdFrontUrl = firebaseStorageService.uploadFile(
                file = idCardFront,
                folder = "craftsmen/$craftsmanId/id-cards",
                fileName = "id-front-${System.currentTimeMillis()}.${getFileExtension(idCardFront)}"
            )

            newIdBackUrl = firebaseStorageService.uploadFile(
                file = idCardBack,
                folder = "craftsmen/$craftsmanId/id-cards",
                fileName = "id-back-${System.currentTimeMillis()}.${getFileExtension(idCardBack)}"
            )

            // Only delete old files after successful upload
            listOfNotNull(oldIdFront, oldIdBack).forEach { url ->
                firebaseStorageService.deleteFileByUrlAsync(url)
            }

            // Update database
            val updatedCraftsman = craftsman.copy(
                verification = craftsman.verification.copy(
                    idCardFront = newIdFrontUrl,
                    idCardBack = newIdBackUrl,
                    verificationStatus = updateVerificationStatus(
                        craftsman.verification.copy(
                            idCardFront = newIdFrontUrl,
                            idCardBack = newIdBackUrl
                        )
                    )
                ),
                updatedAt = Instant.now()
            )

            return craftsmanRepository.save(updatedCraftsman)

        } catch (e: Exception) {
            // Rollback: delete any newly uploaded files
            newIdFrontUrl?.let { firebaseStorageService.deleteFileByUrl(it) }
            newIdBackUrl?.let { firebaseStorageService.deleteFileByUrl(it) }
            throw e
        }
    }

    @Transactional
    fun uploadWorkPortfolio(
        craftsmanId: String,
        userId: String,
        workImages: List<MultipartFile>
    ): Craftsman {
        val craftsman = validateCraftsmanOwnership(craftsmanId, userId)

        // Validate work images
        validateWorkImages(workImages)

        // Delete old work images
        if (craftsman.verification.workVerificationImages.isNotEmpty()) {
            println("Deleting ${craftsman.verification.workVerificationImages.size} old work images")
            firebaseStorageService.deleteMultipleFilesByUrlsAsync(craftsman.verification.workVerificationImages)
        }

        // Upload new work images
        val workUrls = workImages.mapIndexed { index, file ->
            firebaseStorageService.uploadFile(
                file = file,
                folder = "craftsmen/$craftsmanId/work-portfolio",
                fileName = "work-${System.currentTimeMillis()}-$index.${getFileExtension(file)}"
            )
        }

        // Update craftsman
        val updatedCraftsman = craftsman.copy(
            verification = craftsman.verification.copy(
                workVerificationImages = workUrls,
                verificationStatus = updateVerificationStatus(craftsman.verification)
            ),
            updatedAt = Instant.now()
        )

        return craftsmanRepository.save(updatedCraftsman)
    }

    @Transactional
    fun deleteCraftsmanAccount(craftsmanId: String, userId: String): Boolean {
        val craftsman = validateCraftsmanOwnership(craftsmanId, userId)

        // Collect all file URLs
        val allUrls = mutableListOf<String>()
        craftsman.verification.idCardFront?.let { allUrls.add(it) }
        craftsman.verification.idCardBack?.let { allUrls.add(it) }
        allUrls.addAll(craftsman.verification.workVerificationImages)

        // Delete all files
        if (allUrls.isNotEmpty()) {
            firebaseStorageService.deleteMultipleFilesByUrls(allUrls)
        }

        // Delete craftsman record
        craftsmanRepository.deleteById(ObjectId(craftsmanId))

        return true
    }

    fun getCraftsmanStatus(craftsmanId: String): CraftsmanStatusResponse {
        val craftsman = getCraftsmanByDatabaseId(craftsmanId)

        return CraftsmanStatusResponse(
            craftsmanId = craftsman.id?.toHexString() ?: throw NotFoundException("Craftsman ID is null"),
            status = craftsman.status,
            verificationStatus = craftsman.verification.verificationStatus,
            message = when (craftsman.status) {
                CraftsmanStatus.PENDING_VERIFICATION -> "Profile pending verification"
                CraftsmanStatus.ACTIVE -> "Profile active and verified"
                CraftsmanStatus.SUSPENDED -> "Profile suspended"
                CraftsmanStatus.REJECTED -> "Profile rejected: ${craftsman.verification.rejectionReason ?: "No reason provided"}"
            }
        )
    }

    fun getCraftsmanByCraftsmanId(userId: String): Craftsman? {
        return craftsmanRepository.findByUserId(userId)
    }

    fun getCraftsmanByDatabaseId(craftsmanId: String): Craftsman {
        return craftsmanRepository.findById(ObjectId(craftsmanId))
            .orElseThrow { NotFoundException("Craftsman not found") }
    }


    private fun validateCraftsmanOwnership(craftsmanId: String, userId: String): Craftsman {
        val craftsman = craftsmanRepository.findById(ObjectId(craftsmanId))
            .orElseThrow { NotFoundException("Craftsman not found") }

        if (craftsman.userId != userId) {
            throw ForbiddenException("You don't have permission to update this profile")
        }

        return craftsman
    }

    private fun validateWorkImages(files: List<MultipartFile>) {
        val allowedTypes = listOf("image/jpeg", "image/png", "image/jpg")
        val maxSize = 16 * 1024 * 1024 // 16MB for work images

        files.forEach { file ->
            if (!allowedTypes.contains(file.contentType)) {
                throw BadRequestException("Work images must be JPEG or PNG format")
            }
            if (file.size > maxSize) {
                throw BadRequestException("Work image ${file.originalFilename} exceeds 10MB limit")
            }
        }
    }

    private fun validateIdCardFiles(files: List<MultipartFile>) {
        val allowedTypes = listOf("image/jpeg", "image/png", "image/jpg")
        val maxSize = 4 * 1024 * 1024 // 4MB

        files.forEach { file ->
            if (!allowedTypes.contains(file.contentType)) {
                throw BadRequestException("ID cards must be JPEG or PNG format")
            }
            if (file.size > maxSize) {
                throw BadRequestException("ID card image must be less than 5MB")
            }

            // Future: Add ID card validation here
            // validateIdCardContent(file)
        }
    }

    private fun updateVerificationStatus(verification: Verification): VerificationStatus {
        return when {
            verification.idCardFront == null || verification.idCardBack == null -> VerificationStatus.NOT_SUBMITTED
            verification.workVerificationImages.isEmpty() -> VerificationStatus.NOT_SUBMITTED
            else -> VerificationStatus.PENDING
        }
    }

    private fun getFileExtension(file: MultipartFile): String {
        return file.originalFilename?.substringAfterLast('.', "jpg") ?: "jpg"
    }
}