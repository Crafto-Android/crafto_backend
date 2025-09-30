package com.crafto.crafto_backend.service

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Bucket
import com.google.cloud.storage.Storage
import com.google.firebase.cloud.StorageClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.UUID

@ConfigurationProperties(prefix = "storage.firebase")
data class ImageStorageProperties(
    val bucket: String,
    val downloadUrl: String
)

@Service
@EnableConfigurationProperties(ImageStorageProperties::class)
class ImageStorageService(
    private val storageClient: StorageClient,
    private val props: ImageStorageProperties
) {
    fun uploadImage(
        file: MultipartFile,
        fileName: String,
        folderName: String,
    ): String {
        val mimeType =
            file.contentType ?: throw IllegalArgumentException("Content type cannot be null")

        if (!allowedMimeTypes.containsKey(mimeType)) {
            throw IllegalArgumentException("File type $mimeType is not allowed")
        }
        try {
            val extension = allowedMimeTypes[mimeType]!!
            val uniqueFileName =
                "${fileName}_${LocalDateTime.now()}_${UUID.randomUUID()}.$extension"

            val storagePath = "images/$folderName/$uniqueFileName"

            val blobInfo = BlobInfo.newBuilder(BlobId.of(props.bucket, storagePath))
                .setContentType(mimeType)
                .build()

            val storage = storageClient.bucket()
            val blob = storage.create(
                storagePath,
                file.bytes,
                Bucket.BlobTargetOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ)
            )

            // Generate download URL
            return String.format(props.downloadUrl, props.bucket, storagePath)
        } catch (ex: Exception) {
            throw RuntimeException("Failed to upload image to Firebase Storage", ex)
        }
    }

    companion object {
        val allowedMimeTypes = mapOf(
            "image/jpeg" to "jpg",
            "image/jpg" to "jpg",
            "image/png" to "png",
            "image/webp" to "webp",
        )
    }
}