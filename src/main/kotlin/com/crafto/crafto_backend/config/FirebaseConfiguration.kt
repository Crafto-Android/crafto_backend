package com.crafto.crafto_backend.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.cloud.storage.Bucket
import com.google.firebase.cloud.StorageClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import javax.annotation.PostConstruct

@Configuration
class FirebaseConfiguration {

    @Value("\${firebase.storage.bucket}")
    private lateinit var storageBucket: String

    @Value("\${firebase.config.path}")
    private lateinit var firebaseConfigPath: String

    @PostConstruct
    fun initializeFirebase() {
        try {
            // Load service account file from resources folder
            val serviceAccount = ClassPathResource(firebaseConfigPath).inputStream

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(storageBucket)
                .build()

            // Initialize only if not already initialized
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
                println("Firebase initialized successfully with bucket: $storageBucket")
            }
        } catch (e: Exception) {
            println("Error initializing Firebase: ${e.message}")
            throw RuntimeException("Failed to initialize Firebase", e)
        }
    }

    @Bean
    fun firebaseStorage(): Bucket {
        return StorageClient.getInstance().bucket()
    }
}
