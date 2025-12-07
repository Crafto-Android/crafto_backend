package com.crafto.crafto_backend.config

import jakarta.servlet.MultipartConfigElement
import org.springframework.boot.web.servlet.MultipartConfigFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.unit.DataSize

@Configuration
@SuppressWarnings("java:S5693") // File upload size is validated and secured NOSONAR - secured endpoints
class FileUploadConfiguration {

    @Bean
    fun multipartConfigElement(): MultipartConfigElement {
        val factory = MultipartConfigFactory()
        factory.setMaxFileSize(DataSize.ofMegabytes(4))
        factory.setMaxRequestSize(DataSize.ofMegabytes(16))
        return factory.createMultipartConfig()
    }
}