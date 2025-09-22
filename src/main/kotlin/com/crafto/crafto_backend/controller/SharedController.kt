package com.crafto.crafto_backend.controller

import com.crafto.crafto_backend.dto.CategoryResponse
import com.crafto.crafto_backend.service.SharedService
import org.springframework.web.bind.annotation.*

// GET  http://localhost:8085/categories

@RestController
class SharedController(private val service: SharedService) {

    @GetMapping("/categories")
    fun GetAllCategories(): List<CategoryResponse> {
        return service.getAllCategories()
    }
}