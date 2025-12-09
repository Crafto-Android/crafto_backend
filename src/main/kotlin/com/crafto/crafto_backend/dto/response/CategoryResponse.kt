package com.crafto.crafto_backend.dto.response

data class CategoryResponse(
    val categoryId:String,
    val categoryName:String,
    val categoryColor:String,
    val categoryIconUrl:String,
    val categoryDescription:String,
    val popularity: Int,
)