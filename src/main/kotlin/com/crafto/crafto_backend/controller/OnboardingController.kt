package com.crafto.crafto_backend.controller

import com.crafto.crafto_backend.database.entity.OnboardingItem
import com.crafto.crafto_backend.service.OnboardingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/onboarding")
class OnboardingController (
    private val  onboardingService : OnboardingService
){
    @GetMapping
    fun getOnboardingScreens(): List<OnboardingItem> = onboardingService.getOnboardingData()
}