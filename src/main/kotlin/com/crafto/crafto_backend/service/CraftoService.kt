package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.mapper.toCategoryResponse
import com.crafto.crafto_backend.repository.CraftoRepository
import com.crafto.crafto_backend.response.CategoryResponse
import org.springframework.stereotype.Service

@Service
class CraftoService (private val repository: CraftoRepository){

    fun getAllCategories():List<CategoryResponse>{
        return repository.findAll().map { it.toCategoryResponse() }
    }
}