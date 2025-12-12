package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.config.FirebaseStorageProperties
import com.crafto.crafto_backend.constant.AppConstants.FileUpload.allowedMimeTypes
import com.crafto.crafto_backend.service.exception.InvalidImageFormatException
import com.google.cloud.storage.Acl
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.firebase.cloud.StorageClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.net.URLDecoder
import java.util.UUID

@Service
class FirebaseStorageService(
    private val storageClient: StorageClient,
    private val storageProperties: FirebaseStorageProperties
) {
    private val deleteScope = CoroutineScope(Dispatchers.IO)

    fun uploadFile(
        file: MultipartFile,
        folderName: String,
        fileName: String? = null
    ): String {
        try {
            val actualFileName = fileName ?: generateUniqueFileName(file)
            val fullPath = "$folderName/$actualFileName"

            val blobId = BlobId.of(storageProperties.bucket, fullPath)
            val blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.contentType ?: "application/octet-stream")
                .build()

            val firebaseBucket = storageClient.bucket()
            val blob = firebaseBucket.storage.create(blobInfo, file.bytes)
            blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))

            return storageProperties.generateUrl(fullPath)

        } catch (e: Exception) {
            println("Error uploading file: ${e.message}")
            throw RuntimeException("Failed to upload file to Firebase Storage", e)
        }
    }

    fun uploadImage(
        file: MultipartFile,
        folderName: String,
        fileName: String? =null,
    ): String {
        if (file.isEmpty) {
            throw IllegalArgumentException("File is empty")
        }

        val mimeType =
            file.contentType ?: throw IllegalArgumentException("Content type cannot be null")

        val extension = allowedMimeTypes[mimeType]
            ?: throw InvalidImageFormatException()

        //Generate filename with correct extension from MIME type
        val actualFileName = if (fileName != null) {
            val nameWithoutExt = fileName.substringBeforeLast(".", fileName)
            "$nameWithoutExt.$extension"
        } else {
            val timestamp = System.currentTimeMillis()
            val uuid = UUID.randomUUID().toString().take(8)
            "${timestamp}_${uuid}.$extension"
        }
        return uploadFile(file, folderName, actualFileName)
    }

    fun deleteFile(filePath: String): Boolean {
        return try {
            val blobId = BlobId.of(storageProperties.bucket, filePath)
            val firebaseBucket = storageClient.bucket()
            val deleted = firebaseBucket.storage.delete(blobId)
            println("Delete file: $filePath - Result: $deleted")
            deleted
        } catch (e: Exception) {
            println("Error deleting file: ${e.message}")
            false
        }
    }

    fun deleteFileByUrl(fileUrl: String): Boolean {
        return try {
            val filePath = extractFilePathFromUrl(fileUrl)
            if (filePath != null) {
                deleteFile(filePath)
            } else {
                println("Could not extract file path from URL: $fileUrl")
                false
            }
        } catch (e: Exception) {
            println("Error deleting file by URL: ${e.message}")
            false
        }
    }

    fun deleteFileByUrlAsync(fileUrl: String) {
        deleteScope.launch {
            try {
                deleteFileByUrl(fileUrl)
            } catch (e: Exception) {
                println("Async deletion failed: ${e.message}")
            }
        }
    }

    fun deleteMultipleFilesByUrls(fileUrls: List<String>): Int {
        var deletedCount = 0
        fileUrls.forEach { url ->
            if (deleteFileByUrl(url)) {
                deletedCount++
            }
        }
        println("Deleted $deletedCount out of ${fileUrls.size} files")
        return deletedCount
    }

    fun deleteMultipleFilesByUrlsAsync(fileUrls: List<String>) {
        if (fileUrls.isEmpty()) return

        deleteScope.launch {
            var deletedCount = 0
            fileUrls.forEach { url ->
                try {
                    if (deleteFileByUrl(url)) {
                        deletedCount++
                    }
                } catch (e: Exception) {
                    println("Failed to delete: $url")
                }
            }
            println("Async deletion completed: $deletedCount/${fileUrls.size}")
        }
    }

    private fun generateUniqueFileName(file: MultipartFile): String {
        val originalName = file.originalFilename ?: "file"
        val extension = originalName.substringAfterLast('.', "")
        val timestamp = System.currentTimeMillis()
        val uuid = UUID.randomUUID().toString().take(8)

        return if (extension.isNotEmpty()) {
            "${timestamp}_${uuid}.$extension"
        } else {
            "${timestamp}_${uuid}"
        }
    }

    private fun extractFilePathFromUrl(fileUrl: String): String? {
        return try {
            when {
                // Format: https://storage.googleapis.com/bucket-name/path/to/file
                fileUrl.contains("storage.googleapis.com") -> {
                    val uri = URI(fileUrl)
                    val path = uri.path ?: return null

                    if (!path.contains(storageProperties.bucket)) {
                        println("URL doesn't match expected bucket: $fileUrl")
                        return null
                    }

                    path.substringAfter("/${storageProperties.bucket}/")
                }

                // Format: https://firebasestorage.googleapis.com/v0/b/bucket/o/path%2Fto%2Ffile
                fileUrl.contains("firebasestorage.googleapis.com") -> {
                    val uri = URI(fileUrl)
                    val path = uri.path ?: return null

                    if (!path.contains("/o/")) {
                        println("Invalid Firebase Storage URL format: $fileUrl")
                        return null
                    }

                    val encodedPath = path.substringAfter("/o/")
                    URLDecoder.decode(encodedPath, "UTF-8")
                }

                else -> {
                    println("Unknown URL format: $fileUrl")
                    null
                }
            }
        } catch (e: Exception) {
            println("Error extracting file path from URL: ${e.message}")
            null
        }
    }
}