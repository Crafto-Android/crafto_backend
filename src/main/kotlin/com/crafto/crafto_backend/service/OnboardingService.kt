package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.entity.OnboardingItem
import com.crafto.crafto_backend.repository.OnboardingRepository
import org.springframework.stereotype.Service

@Service
class OnboardingService (
    private val repository : OnboardingRepository
){
    fun getOnboardingData():List<OnboardingItem> = repository.findAll()
}
