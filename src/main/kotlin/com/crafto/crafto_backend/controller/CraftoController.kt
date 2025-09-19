package com.crafto.crafto_backend.controller

import com.crafto.crafto_backend.response.CategoryResponse
import com.crafto.crafto_backend.service.CraftoService
import org.springframework.web.bind.annotation.*

// GET  http://localhost:8085/categories

@RestController
@RequestMapping("/categories")
class CraftoController(private val service: CraftoService) {

    @GetMapping
    fun GetAllCategories(): List<CategoryResponse> {
        return service.getAllCategories()
    }
}