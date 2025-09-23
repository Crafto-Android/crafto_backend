package com.crafto.crafto_backend.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document



@Document("District")
data class District(
    @Id
    val id: String? = null,
    val name: String,
)