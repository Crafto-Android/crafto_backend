package com.crafto.crafto_backend.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("governorates")
data class Governorates(
    @Id
    val id: String? = null,
    val name: String
)