package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.constant.AppConstants
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
            profilePictureUrl = null,
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
                folder = AppConstants.StoragePaths.craftsmanIdCards(craftsmanId),
                fileName = "id-front-${System.currentTimeMillis()}.${getFileExtension(idCardFront)}"
            )

            newIdBackUrl = firebaseStorageService.uploadFile(
                file = idCardBack,
                folder = AppConstants.StoragePaths.craftsmanIdCards(craftsmanId),
                fileName = "id-back-${System.currentTimeMillis()}.${getFileExtension(idCardBack)}"
            )

            // Only delete old files after successful upload
            listOfNotNull(oldIdFront, oldIdBack).forEach { url ->
                firebaseStorageService.deleteFileByUrlAsync(url)
            }

            val updatedVerification = craftsman.verification.copy(
                idCardFront = newIdFrontUrl,
                idCardBack = newIdBackUrl,)

            // Update database
            return craftsmanRepository.save(
                craftsman.copy(
                    verification = updatedVerification,
                    updatedAt = Instant.now()
                )
            )

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
                folder = AppConstants.StoragePaths.craftsmanWorkPortfolio(craftsmanId),
                fileName = "work-${System.currentTimeMillis()}-$index.${getFileExtension(file)}"
            )
        }

        // Create updated verification ONCE
        val updatedVerification = craftsman.verification.copy(
            workVerificationImages = workUrls
        )

        // Update craftsman
        val updatedCraftsman = craftsman.copy(
            verification = updatedVerification.copy(
                verificationStatus = updateVerificationStatus(updatedVerification)
            ),
            updatedAt = Instant.now()
        )

        return craftsmanRepository.save(updatedCraftsman)
    }

    @Transactional
    fun uploadProfilePicture(
        craftsmanId: String,
        userId: String,
        profilePicture: MultipartFile
    ): Craftsman {
        val craftsman = validateCraftsmanOwnership(craftsmanId, userId)

        // Validate file
        validateImageFile(profilePicture, "Profile picture")

        // Delete old profile picture if exists
        craftsman.profilePictureUrl?.let { oldUrl ->
            firebaseStorageService.deleteFileByUrlAsync(oldUrl)
        }

        // Upload new profile picture
        val profilePictureUrl = firebaseStorageService.uploadFile(
            file = profilePicture,
            folder = AppConstants.StoragePaths.craftsmanProfilePicture(craftsmanId),
            fileName = "profile-${System.currentTimeMillis()}.${getFileExtension(profilePicture)}"
        )

        // Update craftsman
        val updatedCraftsman = craftsman.copy(
            profilePictureUrl = profilePictureUrl,
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

    private fun validateImageFile(file: MultipartFile, fileType: String) {
        if (file.isEmpty) {
            throw BadRequestException("Empty file uploaded")
        }

        if (!AppConstants.FileUpload.ALLOWED_IMAGE_TYPES.contains(file.contentType)) {
            throw BadRequestException(
                "$fileType must be ${AppConstants.FileUpload.ALLOWED_IMAGE_EXTENSIONS.joinToString()} format"
            )
        }

        if (file.size > AppConstants.FileUpload.MAX_FILE_SIZE_BYTES) {
            throw BadRequestException(
                "$fileType exceeds ${AppConstants.FileUpload.MAX_FILE_SIZE_MB} MB limit"
            )
        }
    }

    private fun validateImageFiles(
        files: List<MultipartFile>,
        fileType: String = "Image",
        maxSizeBytes: Int = AppConstants.FileUpload.MAX_FILE_SIZE_BYTES
    ) {
        files.forEach { file ->
            if (file.isEmpty) {
                throw BadRequestException("Empty file uploaded")
            }

            if (!AppConstants.FileUpload.ALLOWED_IMAGE_TYPES.contains(file.contentType)) {
                throw BadRequestException(
                    "$fileType must be ${AppConstants.FileUpload.ALLOWED_IMAGE_EXTENSIONS} format"
                )
            }

            if (file.size > maxSizeBytes) {
                throw BadRequestException(
                    "$fileType ${file.originalFilename} exceeds ${AppConstants.FileUpload.MAX_FILE_SIZE_MB} MB limit"
                )
            }
        }
    }

    private fun validateWorkImages(files: List<MultipartFile>) {
        validateImageFiles(files, fileType = "Work image")
    }

    private fun validateIdCardFiles(files: List<MultipartFile>) {
        validateImageFiles(files, fileType = "ID card")

        // TODO Future: Add ID card validation here
        // validateIdCardContent(file)
    }

    private fun updateVerificationStatus(verification: Verification): VerificationStatus {
        return when {
            verification.idCardFront == null || verification.idCardBack == null -> VerificationStatus.NOT_SUBMITTED
            verification.workVerificationImages.isEmpty() -> VerificationStatus.NOT_SUBMITTED
            else -> VerificationStatus.PENDING
        }
    }

    private fun getFileExtension(file: MultipartFile): String {
        val filename = file.originalFilename ?: return "jpg"

        return if (filename.contains('.')) {
            val extension = filename.substringAfterLast('.').lowercase()
            // Validate it's an allowed extension
            if (AppConstants.FileUpload.ALLOWED_IMAGE_EXTENSIONS.contains(extension)) {
                extension
            } else {
                "jpg"
            }
        } else {
            "jpg"
        }
    }
}