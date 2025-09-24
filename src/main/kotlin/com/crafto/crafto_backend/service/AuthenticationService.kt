package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.dto.AuthenticationRequest
import com.crafto.crafto_backend.dto.AuthenticationResponse
import com.crafto.crafto_backend.mapper.toAuthenticationEntity
import com.crafto.crafto_backend.mapper.toAuthenticationResponse
import com.crafto.crafto_backend.repository.AuthenticationRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class AuthenticationService(private val authenticationRepository: AuthenticationRepository) {

    fun findByUserId(userId: String): AuthenticationResponse? {
        val response = authenticationRepository.findById(ObjectId(userId)).orElse(null)
        return response.toAuthenticationResponse()
    }

    fun save(body: AuthenticationRequest): AuthenticationResponse {
        val userAuth = authenticationRepository.save(body.toAuthenticationEntity())
        return userAuth.toAuthenticationResponse()
    }
}
