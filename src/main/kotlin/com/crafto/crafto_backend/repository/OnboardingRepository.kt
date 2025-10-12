package com.crafto.crafto_backend.repository

import com.crafto.crafto_backend.entity.OnboardingItem
import org.springframework.data.mongodb.repository.MongoRepository

interface OnboardingRepository : MongoRepository<OnboardingItem, String>