package com.crafto.crafto_backend.database.repository

import com.crafto.crafto_backend.database.entity.OnboardingItem
import org.springframework.data.mongodb.repository.MongoRepository

interface OnboardingRepository : MongoRepository<OnboardingItem, String>