package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.mapper.toCategoryResponse
import com.crafto.crafto_backend.database.repository.CategoryRepository
import com.crafto.crafto_backend.dto.CategoryResponse
import org.springframework.stereotype.Service

@Service
class SharedService (private val repository: CategoryRepository){

    fun getAllCategories():List<CategoryResponse>{
        return repository.findAll().map { it.toCategoryResponse() }
    }
}