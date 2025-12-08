package com.crafto.crafto_backend.controller

import com.crafto.crafto_backend.dto.CategoryResponse
import com.crafto.crafto_backend.database.entity.Category
import com.crafto.crafto_backend.service.SharedService
import org.springframework.web.bind.annotation.*

// GET  http://localhost:8085/categories
// Get  http://localhost:8085/search

@RestController
class SharedController(private val service: SharedService) {

    @GetMapping("/categories")
    fun getAllCategories(
        @RequestParam(name = "sorted", required = false) sorted: Boolean?
    ): List<CategoryResponse> {
        service.getAllCategories().let { categories ->
            if (sorted == true) return  categories.sortedByDescending { it.popularity }
            return categories
        }
    }

    @GetMapping("/search")
    fun search(@RequestParam query: String): List<Category> = service.search(query)
}