package com.crafto.crafto_backend.service

import com.crafto.crafto_backend.mapper.toCategoryResponse
import com.crafto.crafto_backend.repository.CategoryRepository
import com.crafto.crafto_backend.dto.CategoryResponse
import com.crafto.crafto_backend.entity.Category
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria


@Service
class SharedService (
    private val repository: CategoryRepository,
    private val mongoTemplate: MongoTemplate
){

    fun getAllCategories():List<CategoryResponse>{
        return repository.findAll().map { it.toCategoryResponse() }
    }

    fun search(query:String) : List<Category>{
        val pattern=".*${Regex.escape(query)}.*"
        val criteria = Criteria.where(CATEGORY_INDEX_KEY).regex(pattern,"i") // i-> refer to ignore case-insensitive
        val q = Query(criteria).limit(10)
        return mongoTemplate.find(q, Category::class.java)
    }


    companion object {
        const val CATEGORY_INDEX_KEY = "categoryName"
    }
}