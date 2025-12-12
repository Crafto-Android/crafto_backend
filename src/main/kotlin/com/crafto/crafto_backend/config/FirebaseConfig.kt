package com.crafto.crafto_backend.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.StorageClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import javax.annotation.PostConstruct

@Configuration
@EnableConfigurationProperties(FirebaseStorageProperties::class)
class FirebaseConfig {
    @Value("\${firebase.bucket-name}")
    private lateinit var bucketName: String

    @Value("\${firebase.config.path}")
    private lateinit var firebaseConfigPath: String

    @PostConstruct
    fun initializeFirebase() {
        try {
            val serviceAccount = ClassPathResource(firebaseConfigPath).inputStream

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(bucketName)
                .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                println("Firebase initialized successfully with bucket: $bucketName")
            }
        } catch (e: Exception) {
            println("Error initializing Firebase: ${e.message}")
            throw RuntimeException("Failed to initialize Firebase", e)
        }
    }

    @Bean
    fun storageClient(): StorageClient {
        return StorageClient.getInstance()
        }
}