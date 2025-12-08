package com.crafto.crafto_backend.database.entity

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.annotation.Id


@Document("onboarding_Item")
data class OnboardingItem(
    @Id
    val id : String,
    val imageRes : String,
    val title : String,
    val description: String
)