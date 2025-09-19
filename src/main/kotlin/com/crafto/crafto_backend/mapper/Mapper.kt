package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.entity.Category
import com.crafto.crafto_backend.response.CategoryResponse

fun Category.toCategoryResponse(): CategoryResponse {
    return CategoryResponse(
        categoryColor = categoryColor,
        categoryName = categoryName,
    )
}
