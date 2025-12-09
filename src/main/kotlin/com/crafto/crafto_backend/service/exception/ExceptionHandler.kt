package com.crafto.crafto_backend.service.exception

import com.crafto.crafto_backend.dto.response.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class ExceptionHandler {


    @ExceptionHandler(CraftoExectpion::class)
    fun handleCraftoException(exception: CraftoExectpion, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            code = exception.code,
            message = exception.message,
            statusCode = exception.httpStatus,
            path = request.requestURI,
            error = exception.httpStatus.reasonPhrase
        )
        return ResponseEntity.status(exception.httpStatus).body(response)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(exception: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            code = ErrorCode.GEN_INTERNAL_SERVER_ERROR,
            message = exception.message ?: "Internal error",
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
            path = request.requestURI,
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(exception: CraftoExectpion, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            code = exception.code,
            message = exception.message,
            statusCode = HttpStatus.BAD_REQUEST,
            path = request.requestURI,
            error = HttpStatus.BAD_REQUEST.reasonPhrase
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(exception: CraftoExectpion, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse(
            code = exception.code,
            message = exception.message,
            statusCode = HttpStatus.BAD_REQUEST,
            path = request.requestURI,
            error = HttpStatus.BAD_REQUEST.reasonPhrase
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(response)
    }

}