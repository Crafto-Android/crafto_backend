package com.crafto.crafto_backend.service.exception

object ErrorCode {

    // General
    const val GEN_VALIDATION_ERROR = "GEN_001"
    const val GEN_REQUEST_BODY_ERROR = "GEN_002"
    const val GEN_INTERNAL_SERVER_ERROR = "GEN_003"

    // User
    const val USER_PHONE_NUMBER_ALREADY_EXIST = "USER_001"
    const val USER_NOT_FOUND = "USER_002"
    const val USER_ALREADY_EXIST = "USER_003"

    // Phone number
    const val INVALID_PHONE_NUMBER = "PHONE_001"
    const val INVALID_PHONE_NUMBER_REGION = "PHONE_002"

    // OTP
    const val OTP_EXPIRED = "OTP_001"
    const val OTP_INVALID = "OTP_002"

    //Issues
    const val ISSUE_NOT_FOUND = "ISSUE_001"

    // Offer
    const val OFFER_NOT_FOUND = "OFFER_001"

    //Categories
    const val CATEGORY_NOT_FOUND = "CATEGORY_001"

    //Image
    const val IMAGE_UPLOAD_FAILED = "IMAGE_001"
    const val IMAGE_INVALID_FORMAT = "IMAGE_002"
    const val IMAGE_INVALID_SIZE = "IMAGE_003"
}