package com.crafto.crafto_backend.mapper

import com.crafto.crafto_backend.entity.Category
import com.crafto.crafto_backend.dto.CategoryResponse

fun Category.toCategoryResponse(): CategoryResponse {
    return CategoryResponse(
        categoryId=id,
        categoryColor = categoryColor,
        categoryName = categoryName,
        categoryDescription = categoryDescription,
        categoryIconUrl = categoryIconUrl,
        popularity = popularity
    )
}
