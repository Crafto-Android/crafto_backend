package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.entity.Governorates
import com.crafto.crafto_backend.dto.GovernorateResponse

fun Governorates.toGovernorateResponse(): GovernorateResponse {
    return GovernorateResponse(
        id = id ?: "",
        name = name
    )
}