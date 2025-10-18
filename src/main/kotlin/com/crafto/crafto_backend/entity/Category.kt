package com.crafto.crafto_backend.entity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("categories")
data class Category(
    @Id
    val id: String,
    @TextIndexed
    val categoryName:String,
    val categoryColor:String
)
