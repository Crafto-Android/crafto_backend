package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.entity.Governorate
import com.crafto.crafto_backend.dto.GovernorateResponse

fun Governorate.toGovernorateResponse(): GovernorateResponse {
    return GovernorateResponse(
        id = id ?: "",
        name = name
    )
}
