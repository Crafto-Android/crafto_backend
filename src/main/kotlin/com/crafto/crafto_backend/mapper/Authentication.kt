package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.dto.request.AuthorizationRequest
import com.crafto.crafto_backend.dto.response.AuthorizationResponse
import com.crafto.crafto_backend.database.entity.Authorization
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