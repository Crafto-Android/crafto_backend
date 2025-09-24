package com.crafto.crafto_backend.entity

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("Authentication")
data class Authorization(
    @Id val userId: ObjectId,
    val isCustomer: Boolean,
)
