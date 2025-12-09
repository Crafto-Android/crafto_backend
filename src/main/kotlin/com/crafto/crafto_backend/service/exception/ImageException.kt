package com.crafto.crafto_backend.service.exception

import org.springframework.http.HttpStatus

class UploadImageException:CraftoExectpion(
    code = ErrorCode.IMAGE_UPLOAD_FAILED,
    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    message = "Image upload failed"
)

class InvalidImageFormatException:CraftoExectpion(
    code = ErrorCode.IMAGE_INVALID_FORMAT,
    httpStatus = HttpStatus.BAD_REQUEST,
    message = "Invalid image format"
)

class InvalidImageSize:CraftoExectpion(
    code = ErrorCode.IMAGE_INVALID_SIZE,
    httpStatus = HttpStatus.BAD_REQUEST,
    message = "Invalid image size"
)
