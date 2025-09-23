package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.dto.DistrictResponse
import com.crafto.crafto_backend.entity.District

fun District.toAreaResponse(): DistrictResponse {
    return DistrictResponse(
        id = id ?: "",
        name = name
    )
}