package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.database.entity.District
import com.crafto.crafto_backend.dto.response.DistrictResponse

fun District.toAreaResponse(): DistrictResponse {
    return DistrictResponse(
        id = id ?: "",
        name = name,
        governorateId = governorateId
    )
}