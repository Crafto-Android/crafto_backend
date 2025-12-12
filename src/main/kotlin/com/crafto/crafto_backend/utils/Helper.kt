package com.crafto.crafto_backend.utils

import com.crafto.crafto_backend.constant.AppConstants
import org.apache.coyote.BadRequestException
import org.springframework.web.multipart.MultipartFile
import kotlin.collections.contains
import kotlin.collections.joinToString
import kotlin.text.contains
import kotlin.text.lowercase
import kotlin.text.substringAfterLast

fun getFileExtension(file: MultipartFile): String {
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

fun validateImageFile(file: MultipartFile, fileType: String,
                      maxSizeBytes: Int = AppConstants.FileUpload.MAX_FILE_SIZE_BYTES) {
    if (file.isEmpty) {
        throw BadRequestException("Empty file uploaded")
    }

    if (!AppConstants.FileUpload.allowedMimeTypes.contains(file.contentType)) {
        throw BadRequestException(
            "$fileType must be ${AppConstants.FileUpload.ALLOWED_IMAGE_EXTENSIONS.joinToString()} format"
        )
    }

    if (file.size > maxSizeBytes) {
        throw BadRequestException(
            "$fileType exceeds ${maxSizeBytes / 1024 / 1024} MB limit"
        )
    }
}

fun validateImageFiles(
    files: List<MultipartFile>,
    fileType: String = "Image",
    maxSizeBytes: Int = AppConstants.FileUpload.MAX_FILE_SIZE_BYTES
) {
    files.forEach { file ->
        if (file.isEmpty) {
            throw BadRequestException("Empty file uploaded")
        }

        if (!AppConstants.FileUpload.allowedMimeTypes.contains(file.contentType)) {
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