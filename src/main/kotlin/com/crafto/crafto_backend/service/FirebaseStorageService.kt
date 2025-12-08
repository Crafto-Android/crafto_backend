package com.crafto.crafto_backend.service

import com.google.cloud.storage.Acl
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Bucket
import com.google.firebase.cloud.StorageClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.net.URLDecoder
import java.util.UUID
import kotlin.collections.forEach
import kotlin.text.contains
import kotlin.text.substringAfter

@Service
@EnableConfigurationProperties(ImageStorageProperties::class)
class FirebaseStorageService(
    private val storageClient: StorageClient,
    private val props: ImageStorageProperties
) {

    private val deleteScope = CoroutineScope(Dispatchers.IO)

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
            val blobId = BlobId.of(props.bucket, fullPath)
            val blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.contentType ?: "application/octet-stream")
                .build()

            // Upload the file
            val blob = storageClient.bucket().storage.create(blobInfo, file.bytes)

            // Make the file publicly readable
            blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))

            // Now this URL will work
            return "https://storage.googleapis.com/${props.bucket}/$fullPath"

        } catch (e: Exception) {
            println("Error uploading file: ${e.message}")
            throw kotlin.RuntimeException("Failed to upload file to Firebase Storage", e)
        }
    }

    fun deleteFile(filePath: String): Boolean {
        return try {
            val blobId = BlobId.of(props.bucket, filePath)
            val deleted = storageClient.bucket().storage.delete(blobId)
            println("File deleted successfully: $filePath - Result: $deleted")
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
        deleteScope.launch {
            fileUrls.forEach { url ->
                try {
                    deleteFileByUrl(url)
                } catch (e: Exception) {
                    println("Failed to delete: $url")
                }
            }
        }
    }

    private fun extractFilePathFromUrl(fileUrl: String): String? {
        return try {
            // Firebase Storage URLs have different formats:
            // 1. Signed URL: https://storage.googleapis.com/bucket-name/path/to/file?GoogleAccessId=...
            // 2. Public URL: https://storage.googleapis.com/bucket-name/path/to/file
            // 3. Firebase URL: https://firebasestorage.googleapis.com/v0/b/bucket-name/o/path%2Fto%2Ffile?...

            when {
                // Handle standard Google Storage URLs
                fileUrl.contains("storage.googleapis.com") -> {
                    val uri = URI(fileUrl)
                    val path = uri.path ?: return null

                    // Ensure bucket name is in the path
                    if (!path.contains(props.bucket)) {
                        println("URL doesn't match expected bucket: $fileUrl")
                        return null
                    }

                    // Remove bucket name from path
                    path.substringAfter("/${props.bucket}/")
                }

                // Handle Firebase Storage URLs
                fileUrl.contains("firebasestorage.googleapis.com") -> {
                    val uri = URI(fileUrl)
                    val path = uri.path ?: return null

                    // Extract the encoded file path after /o/
                    if (!path.contains("/o/")) {
                        println("Invalid Firebase Storage URL format: $fileUrl")
                        return null
                    }
                    // Extract the encoded file path after /o/
                    val encodedPath = path.substringAfter("/o/")
                    // Decode the URL encoding
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