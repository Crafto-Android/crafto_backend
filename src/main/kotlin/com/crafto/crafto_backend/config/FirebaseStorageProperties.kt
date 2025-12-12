package com.crafto.crafto_backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "firebase.storage")
data class FirebaseStorageProperties(
    val bucket: String,
    val downloadUrl: String
) {
    fun generateUrl(filePath: String): String {
        return String.format(downloadUrl, bucket, filePath)
    }
}
