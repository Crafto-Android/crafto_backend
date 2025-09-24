package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.dto.AuthenticationRequest
import com.crafto.crafto_backend.dto.AuthenticationResponse
import com.crafto.crafto_backend.entity.Authentication
import org.bson.types.ObjectId

fun Authentication.toAuthenticationResponse(): AuthenticationResponse {
    return AuthenticationResponse(
        userId = userId.toHexString(),
        isCustomer = isCustomer,
    )
}

fun AuthenticationRequest.toAuthenticationEntity(): Authentication {
    return Authentication(
        userId = userId?.let {ObjectId(it)}?:ObjectId(),
        isCustomer = isCustomer,
    )
}