package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.dto.AuthorizationRequest
import com.crafto.crafto_backend.dto.AuthorizationResponse
import com.crafto.crafto_backend.entity.Authorization
import org.bson.types.ObjectId

fun Authorization.toAuthorizationResponse(): AuthorizationResponse {
    return AuthorizationResponse(
        userId = userId.toHexString(),
        isCustomer = isCustomer,
    )
}

fun AuthorizationRequest.toAuthorizationEntity(): Authorization {
    return Authorization(
        userId = userId?.let {ObjectId(it)}?:ObjectId(),
        isCustomer = isCustomer,
    )
}