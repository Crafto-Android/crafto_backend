package com.crafto.crafto_backend.constant

object AppConstants {

    // File upload limits
    object FileUpload {
        const val MAX_FILE_SIZE_MB = 4
        const val MAX_REQUEST_SIZE_MB = 16
        const val MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024
        const val MAX_REQUEST_SIZE_BYTES = MAX_REQUEST_SIZE_MB * 1024 * 1024
        val ALLOWED_IMAGE_TYPES = listOf("image/jpeg", "image/png", "image/jpg")
        val ALLOWED_IMAGE_EXTENSIONS = listOf("jpg", "jpeg", "png")
    }

    // Firebase storage paths
    object StoragePaths {
        const val CRAFTSMEN = "craftsmen"
        const val ID_CARDS = "id-cards"
        const val WORK_PORTFOLIO = "work-portfolio"
        const val PROFILE_PICTURES = "profile-pictures"
        const val CATEGORY_ICONS = "category-icons"
        const val TEST_UPLOADS = "test-uploads"

        fun craftsmanIdCards(craftsmanId: String) = "$CRAFTSMEN/$craftsmanId/$ID_CARDS"
        fun craftsmanWorkPortfolio(craftsmanId: String) = "$CRAFTSMEN/$craftsmanId/$WORK_PORTFOLIO"
    }

    // Validation constants
    object Validation {
        const val PHONE_REGEX = "^\\+?[1-9]\\d{1,14}$"
        const val MIN_CATEGORIES = 1
        const val MAX_CATEGORIES = 5
        const val MIN_WORK_IMAGES = 1
        const val MAX_WORK_IMAGES = 10
    }

    // Default values
    object Defaults {
        const val SIGNED_URL_EXPIRY_DAYS = 7L
        const val MAX_UPLOAD_ATTEMPTS_PER_HOUR = 10
        const val DEFAULT_PAGE_SIZE = 20
    }
}