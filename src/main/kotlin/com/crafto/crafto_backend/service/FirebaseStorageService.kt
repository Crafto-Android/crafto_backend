package com.crafto.crafto_backend.service

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Bucket
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class FirebaseStorageService(
    private val firebaseBucket: Bucket
) {

    fun uploadFile(
        file: MultipartFile,
        folder: String,
        fileName: String? = null
    ): String {
        try {
            // Generate unique filename if not provided
            val actualFileName = fileName ?: "${UUID.randomUUID()}_${file.originalFilename}"
            val fullPath = "$folder/$actualFileName"

            // Create blob (file) in Firebase Storage
            val blobId = BlobId.of(firebaseBucket.name, fullPath)
            val blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.contentType ?: "application/octet-stream")
                .build()

            // Upload the file
            val blob = firebaseBucket.storage.create(blobInfo, file.bytes)

            // Generate a signed URL (temporary link valid for 7 days)
            val url = blob.signUrl(7, TimeUnit.DAYS).toString()

            println("File uploaded successfully: $fullPath")
            return url

        } catch (e: Exception) {
            println("Error uploading file: ${e.message}")
            throw RuntimeException("Failed to upload file to Firebase Storage", e)
        }
    }

    fun deleteFile(filePath: String): Boolean {
        return try {
            val blobId = BlobId.of(firebaseBucket.name, filePath)
            firebaseBucket.storage.delete(blobId)
            true
        } catch (e: Exception) {
            println("Error deleting file: ${e.message}")
            false
        }
    }

    fun deleteMultipleFiles(filePaths: List<String>) {
        filePaths.forEach { path ->
            deleteFile(path)
        }
    }
}