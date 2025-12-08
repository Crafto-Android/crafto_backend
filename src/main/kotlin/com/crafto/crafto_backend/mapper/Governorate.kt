package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.database.entity.Governorates
import com.crafto.crafto_backend.dto.GovernorateResponse

fun Governorates.toGovernorateResponse(): GovernorateResponse {
    return GovernorateResponse(
        id = id ?: "",
        name = name
    )
}