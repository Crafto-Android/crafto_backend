package com.crafto.crafto_backend.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.StorageClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException
import java.io.InputStream

@Configuration
class FirebaseConfig {
    @Value("\${firebase.bucket-name}")
    private lateinit var bucketName: String

    @Bean
    fun firebaseStorage(): StorageClient {
        try {
            val serviceAccount: InputStream = ClassPathResource("firebase-service-account.json").inputStream

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(bucketName)
                .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
            }

            return StorageClient.getInstance()
        } catch (e: IOException) {
            throw RuntimeException("Failed to initialize Firebase Storage", e)
        }
    }
}