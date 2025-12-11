package com.crafto.crafto_backend.dto.response

import org.springframework.http.HttpStatus
import java.time.Instant

data class ErrorResponse(
    val code: String,
    val message: String,
    val timestamp: Instant = Instant.now(),
    val path : String,
    val statusCode : HttpStatus,
    val error : String
)
