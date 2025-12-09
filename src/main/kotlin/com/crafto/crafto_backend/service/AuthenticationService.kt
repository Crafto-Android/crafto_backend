package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.dto.request.AuthorizationRequest
import com.crafto.crafto_backend.dto.response.AuthorizationResponse
import com.crafto.crafto_backend.mapper.toAuthorizationEntity
import com.crafto.crafto_backend.mapper.toAuthorizationResponse
import com.crafto.crafto_backend.database.repository.AuthorizationRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class AuthenticationService(private val authorizationRepository: AuthorizationRepository) {

    fun findByUserId(userId: String): AuthorizationResponse? {
        val response = authorizationRepository.findById(ObjectId(userId)).orElse(null)
        return response.toAuthorizationResponse()
    }

    fun save(body: AuthorizationRequest): AuthorizationResponse {
        val userAuth = authorizationRepository.save(body.toAuthorizationEntity())
        return userAuth.toAuthorizationResponse()
    }
}
