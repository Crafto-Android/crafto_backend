package com.crafto.crafto_backend.database.entity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("categories")
data class Category(
    @Id
    val id: String,
    val categoryName:String,
    val categoryColor:String,
    val categoryIconUrl:String,
    val categoryDescription:String,
    val popularity: Int = 0,
)
