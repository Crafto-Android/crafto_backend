package com.crafto.crafto_backend.config

import com.crafto.crafto_backend.constant.AppConstants.FileUpload.MAX_FILE_SIZE_MB
import com.crafto.crafto_backend.constant.AppConstants.FileUpload.MAX_REQUEST_SIZE_MB
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
        factory.setMaxFileSize(DataSize.ofMegabytes(MAX_FILE_SIZE_MB.toLong()))
        factory.setMaxRequestSize(DataSize.ofMegabytes(MAX_REQUEST_SIZE_MB.toLong()))
        return factory.createMultipartConfig()
    }
}